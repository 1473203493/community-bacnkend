
#通知表新增一个role角色字段
ALTER TABLE notification
ADD COLUMN role VARCHAR(50) NULL COMMENT '角色类型: 1表示普通学生/2表示社团管理员, 3表示平台管理员' AFTER admin_id;