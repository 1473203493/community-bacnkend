package com.club.controller;



import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.FileUploadService;
import com.club.util.AliOssUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author ljl
 * @create 2023-10-26-14:35
 */

@Tag(name = "图片（文件）接口")
@RestController
@RequestMapping("/admin/system")
@Slf4j
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService ;

    @Autowired
    private AliOssUtil aliOssUtil ;



    /**
     * 文件上传,使用的是OSS
     * @param file
     * @return
     */
    @PostMapping("/oss/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.build(filePath, ResultCodeEnum.SUCCESS);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }

        return Result.build(null, ResultCodeEnum.DATA_ERROR);
    }

}