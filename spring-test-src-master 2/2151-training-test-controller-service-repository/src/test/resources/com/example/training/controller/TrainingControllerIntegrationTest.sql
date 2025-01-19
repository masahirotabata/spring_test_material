INSERT INTO training
(id, title, start_date_time, end_date_time, reserved, capacity) VALUES
('t01', 'ビジネスマナー研修', '2023-08-01 09:30', '2023-08-03 17:00', 1, 10)
,('t02', 'Java実践', '2023-09-01 09:30', '2023-09-03 17:00', 1, 5)
,('t03', 'マーケティング研修', '2023-10-01 09:30', '2023-10-03 17:00', 5, 5)
;

INSERT INTO student_type
(id, code, name) VALUES
('st01', 'EMPLOYEE', '会社員')
,('st02', 'STUDENT', '学生')
,('st03', 'FREELANCE', 'フリーランス')
;
