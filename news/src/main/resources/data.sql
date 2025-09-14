-- 초기 데이터: 이미 토큰을 발급받은 고객사들
INSERT INTO CUSTOMER (customer_name, token, is_active, created_at, last_connected_at) VALUES
('삼성전자', 'QRAFT-SAMSUNG-001', true, NOW(), null),
('LG전자', 'QRAFT-LG-002', true, NOW(), null),
('현대자동차', 'QRAFT-HYUNDAI-003', true, NOW(), null);