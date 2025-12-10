# 插入这条默认系统账号，这个系统账号用于未登录时，保证操作日志AOP没有问题

INSERT INTO `admin` VALUES (1, 'SYS001', 'system', '$2a$10$rOzJmZKzZ1Wv.QVcCNz.PuqDdB.EG6NL/TGXZoUh.0EYNYTloVmgy', '系统管理员', 'system@example.com', 'platform_admin', 'active', '2025-11-23 17:43:43');
