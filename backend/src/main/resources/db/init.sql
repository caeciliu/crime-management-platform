CREATE DATABASE IF NOT EXISTS crime_management_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE crime_management_db;

CREATE TABLE IF NOT EXISTS crime_events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    crime_type VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    location VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    incident_time DATETIME NOT NULL,
    report_time DATETIME,
    status VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '待处理',
    severity_level VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_incident_time (incident_time),
    INDEX idx_crime_type (crime_type),
    INDEX idx_status (status),
    INDEX idx_severity_level (severity_level)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 插入一些示例犯罪事件数据
INSERT INTO crime_events (title, crime_type, location, description, incident_time, severity_level, status) VALUES
('商业街盗窃案', '盗窃', '市中心商业街A区', '一家珠宝店发生盗窃，损失价值约10万元', '2023-10-15 14:30:00', '中等', '调查中'),
('住宅区入室抢劫', '抢劫', '城东花园小区B栋', '歹徒持刀闯入住宅，抢走现金和贵重物品', '2023-10-18 22:15:00', '严重', '已结案'),
('公共场所斗殴事件', '暴力', '中央公园南门', '多名青年发生争执并演变为斗殴，造成2人受伤', '2023-10-20 19:45:00', '中等', '处理中'),
('网络诈骗案件', '诈骗', '线上', '受害者通过虚假投资平台被骗20万元', '2023-10-22 10:20:00', '严重', '待处理'),
('车辆被盗事件', '盗窃', '城北停车场C区', '一辆黑色轿车在停车场被盗', '2023-10-25 08:00:00', '中等', '调查中');