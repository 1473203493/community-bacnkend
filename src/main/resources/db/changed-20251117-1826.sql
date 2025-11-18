#数据表中已经有测试数据了，先设置空字段，填写后再改成非空字段
ALTER TABLE activity
    ADD COLUMN start_time DATETIME NULL COMMENT '活动开始时间',
    ADD COLUMN end_time DATETIME NULL COMMENT '活动结束时间';
UPDATE activity
SET start_time = '1970-01-01 00:00:00';
UPDATE activity
SET end_time = '2970-01-01 00:00:00';
ALTER TABLE activity
    MODIFY COLUMN start_time DATETIME NOT NULL,
    MODIFY COLUMN end_time DATETIME NOT NULL;

# 数据表没有数据直接添加非空
ALTER TABLE activity
    ADD COLUMN start_time DATETIME NOT NULL AFTER time,
    ADD COLUMN end_time DATETIME NOT NULL AFTER start_time;

