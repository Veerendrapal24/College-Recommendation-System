-- SQL Seeding Script for College Recommendation System

-- 1. Insert Colleges
INSERT IGNORE INTO colleges (college_id, name, location, sector, hostel_available, budget_range) VALUES
(1, 'Sri Chaitanya Junior College', 'Hyderabad', 'Private', 1, 95000.00),
(2, 'Narayana Junior College', 'Hyderabad', 'Private', 1, 110000.00),
(3, 'Government Junior College for Boys', 'Hyderabad', 'Government', 0, 3500.00),
(4, 'APRJC (Andhra Pradesh Residential Junior College)', 'Guntur', 'Government', 1, 5000.00),
(5, 'Aditya Junior College', 'Visakhapatnam', 'Private', 1, 80000.00),
(6, 'Sri Chaitanya Junior College', 'Vijayawada', 'Private', 1, 120000.00),
(7, 'Government Junior College', 'Vijayawada', 'Government', 0, 2000.00),
(8, 'Narayana Junior College', 'Visakhapatnam', 'Private', 1, 100000.00),
(9, 'Sri Venkateswara Junior College', 'Tirupati', 'Private', 0, 65000.00),
(10, 'Government Junior College', 'Tirupati', 'Government', 1, 4000.00),
(11, 'Little Flower Junior College', 'Hyderabad', 'Private', 0, 75000.00),
(12, 'St. Patricks Junior College', 'Guntur', 'Private', 0, 55000.00),
(13, 'Government Junior College', 'Guntur', 'Government', 0, 1800.00),
(14, 'Kakatiya Junior College', 'Hyderabad', 'Private', 1, 90000.00),
(15, 'Masterji Junior College', 'Hanamkonda', 'Private', 1, 85000.00),
(16, 'Government Junior College', 'Hanamkonda', 'Government', 0, 2500.00),
(17, 'Gayathri Junior College', 'Visakhapatnam', 'Private', 0, 60000.00),
(18, 'Vikas Junior College', 'Guntur', 'Private', 1, 75000.00),
(19, 'Government Junior College', 'Nellore', 'Government', 1, 3000.00),
(20, 'Sri Chaitanya Junior College', 'Tirupati', 'Private', 1, 105000.00),
(21, 'Narayana Junior College', 'Vijayawada', 'Private', 1, 115000.00),
(22, 'Government Junior College for Girls', 'Guntur', 'Government', 1, 4500.00),
(23, 'Loyola Junior College', 'Vijayawada', 'Private', 1, 70000.00),
(24, 'Government Junior College', 'Kadapa', 'Government', 0, 2200.00),
(25, 'Chaitanya Junior College', 'Kurnool', 'Private', 1, 82000.00);

-- 2. Insert Branches
INSERT IGNORE INTO branches (branch_id, college_id, stream_name) VALUES
-- Sri Chaitanya Hyderabad (1)
(1, 1, 'MPC'), (2, 1, 'BiPC'), (3, 1, 'MEC'), (4, 1, 'CEC'),
-- Narayana Hyderabad (2)
(5, 2, 'MPC'), (6, 2, 'BiPC'),
-- Gov Boys Hyderabad (3)
(7, 3, 'MPC'), (8, 3, 'CEC'),
-- APRJC Guntur (4)
(9, 4, 'MPC'), (10, 4, 'BiPC'), (11, 4, 'MEC'),
-- Aditya Visakhapatnam (5)
(12, 5, 'MPC'), (13, 5, 'BiPC'), (14, 5, 'MEC'),
-- Sri Chaitanya Vijayawada (6)
(15, 6, 'MPC'), (16, 6, 'BiPC'),
-- Gov Vijayawada (7)
(17, 7, 'MPC'), (18, 7, 'CEC'), (19, 7, 'MEC'),
-- Narayana Visakhapatnam (8)
(20, 8, 'MPC'), (21, 8, 'BiPC'), (22, 8, 'CEC'),
-- Sri Venkateswara Tirupati (9)
(23, 9, 'MPC'), (24, 9, 'BiPC'), (25, 9, 'MEC'), (26, 9, 'CEC'),
-- Gov Tirupati (10)
(27, 10, 'MPC'), (28, 10, 'BiPC'), (29, 10, 'CEC'),
-- Little Flower Hyderabad (11)
(30, 11, 'MPC'), (31, 11, 'MEC'), (32, 11, 'CEC'),
-- St. Patricks Guntur (12)
(33, 12, 'MEC'), (34, 12, 'CEC'),
-- Gov Guntur (13)
(35, 13, 'MPC'), (36, 13, 'BiPC'), (37, 13, 'CEC'),
-- Kakatiya Hyderabad (14)
(38, 14, 'MPC'), (39, 14, 'BiPC'),
-- Masterji Hanamkonda (15)
(40, 15, 'MPC'), (41, 15, 'BiPC'), (42, 15, 'MEC'),
-- Gov Hanamkonda (16)
(43, 16, 'MPC'), (44, 16, 'CEC'),
-- Gayathri Visakhapatnam (17)
(45, 17, 'MPC'), (46, 17, 'BiPC'), (47, 17, 'MEC'), (48, 17, 'CEC'),
-- Vikas Guntur (18)
(49, 18, 'MPC'), (50, 18, 'BiPC'),
-- Gov Nellore (19)
(51, 19, 'MPC'), (52, 19, 'BiPC'),
-- Sri Chaitanya Tirupati (20)
(53, 20, 'MPC'), (54, 20, 'BiPC'),
-- Narayana Vijayawada (21)
(55, 21, 'MPC'), (56, 21, 'BiPC'),
-- Gov Girls Guntur (22)
(57, 22, 'MPC'), (58, 22, 'BiPC'), (59, 22, 'CEC'),
-- Loyola Vijayawada (23)
(60, 23, 'MPC'), (61, 23, 'MEC'), (62, 23, 'CEC'),
-- Gov Kadapa (24)
(63, 24, 'MPC'), (64, 24, 'BiPC'), (65, 24, 'MEC'), (66, 24, 'CEC'),
-- Chaitanya Kurnool (25)
(67, 25, 'MPC'), (68, 25, 'BiPC');

-- 3. Insert Performance Metrics
INSERT IGNORE INTO performance_metrics (performance_id, college_id, pass_percentage, neet_qual_percent, mains_qual_percent, ssc_cutoff) VALUES
(1, 1, 95.50, 45.00, 52.00, 9.20),
(2, 2, 96.00, 48.00, 55.00, 9.50),
(3, 3, 72.00, 10.00, 12.00, 6.50),
(4, 4, 98.00, 65.00, 70.00, 9.70),
(5, 5, 88.50, 35.00, 40.00, 8.80),
(6, 6, 97.20, 50.00, 58.00, 9.60),
(7, 7, 75.00, 8.00, 10.00, 7.00),
(8, 8, 94.00, 42.00, 50.00, 9.30),
(9, 9, 85.00, 25.00, 30.00, 8.20),
(10, 10, 78.00, 15.00, 18.00, 7.50),
(11, 11, 92.00, 0.00, 35.00, 8.50),
(12, 12, 83.00, 0.00, 0.00, 8.00),
(13, 13, 70.00, 12.00, 8.00, 6.80),
(14, 14, 91.00, 38.00, 42.00, 9.00),
(15, 15, 89.00, 30.00, 38.00, 8.70),
(16, 16, 74.00, 5.00, 7.00, 6.90),
(17, 17, 82.00, 20.00, 25.00, 8.00),
(18, 18, 90.00, 40.00, 45.00, 9.10),
(19, 19, 80.00, 18.00, 20.00, 7.80),
(20, 20, 94.80, 44.00, 51.00, 9.40),
(21, 21, 95.80, 46.00, 54.00, 9.50),
(22, 22, 82.50, 16.00, 15.00, 7.90),
(23, 23, 86.00, 0.00, 28.00, 8.30),
(24, 24, 76.50, 14.00, 12.00, 7.20),
(25, 25, 87.00, 32.00, 36.00, 8.60);
