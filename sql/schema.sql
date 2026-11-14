CREATE DATABASE IF NOT EXISTS media_team_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE media_team_db;

-- 1. 系统登录账号表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    display_name VARCHAR(50) NOT NULL COMMENT '显示名称',
    role VARCHAR(20) NOT NULL DEFAULT 'MEMBER' COMMENT '角色: ADMIN/MEMBER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统账号表';

-- 2. 团队成员表
DROP TABLE IF EXISTS team_member;
CREATE TABLE team_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    position VARCHAR(50) NOT NULL COMMENT '岗位: 编导/摄影师/剪辑师/运营/商务',
    department VARCHAR(50) NOT NULL COMMENT '所属小组',
    phone VARCHAR(30) DEFAULT '' COMMENT '联系电话',
    base_salary DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '基本月薪',
    status VARCHAR(20) NOT NULL DEFAULT '在职' COMMENT '状态: 在职/休假/离职',
    join_date DATE NOT NULL COMMENT '入职日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团队成员表';

-- 3. 设备台账表
DROP TABLE IF EXISTS team_equipment;
CREATE TABLE team_equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_code VARCHAR(40) NOT NULL UNIQUE COMMENT '资产编号',
    name VARCHAR(80) NOT NULL COMMENT '设备名称',
    category VARCHAR(40) NOT NULL COMMENT '分类: 相机/镜头/灯光/收音/稳定器/电脑',
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '购入价格',
    status VARCHAR(20) NOT NULL DEFAULT '在库' COMMENT '状态: 在库/借出/维修',
    borrower_id BIGINT NULL COMMENT '借用成员ID',
    borrower_name VARCHAR(50) DEFAULT '' COMMENT '借用人姓名',
    borrowed_at VARCHAR(30) DEFAULT '' COMMENT '借出时间',
    remark VARCHAR(200) DEFAULT '' COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_equip_member FOREIGN KEY (borrower_id) REFERENCES team_member(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备台账表';

-- 4. 薪资记录表
DROP TABLE IF EXISTS team_salary;
CREATE TABLE team_salary (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL COMMENT '成员ID',
    member_name VARCHAR(50) NOT NULL COMMENT '成员姓名',
    pay_month VARCHAR(7) NOT NULL COMMENT '发放月份 YYYY-MM',
    base_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '基本工资',
    bonus_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '绩效奖金',
    allowance_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '补贴',
    deduction_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '扣款',
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '实发工资',
    status VARCHAR(20) NOT NULL DEFAULT '待发放' COMMENT '状态: 待发放/已发放',
    remark VARCHAR(200) DEFAULT '' COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_salary_member FOREIGN KEY (member_id) REFERENCES team_member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资记录表';

-- 5. 项目进度表
DROP TABLE IF EXISTS team_project;
CREATE TABLE team_project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL COMMENT '项目/视频选题名称',
    platform VARCHAR(30) NOT NULL COMMENT '平台: 抖音/B站/小红书/视频号',
    owner_id BIGINT NULL COMMENT '负责人ID',
    owner_name VARCHAR(50) NOT NULL COMMENT '负责人姓名',
    stage VARCHAR(30) NOT NULL DEFAULT '选题策划' COMMENT '阶段: 选题策划/脚本撰写/拍摄录制/后期剪辑/审片待发/已发布',
    progress INT NOT NULL DEFAULT 10 COMMENT '进度百分比 0-100',
    due_date DATE NOT NULL COMMENT '计划发布日期',
    remark VARCHAR(255) DEFAULT '' COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_member FOREIGN KEY (owner_id) REFERENCES team_member(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目进度表';

-- 初始化演示账号
INSERT INTO sys_user (username, password, display_name, role) VALUES
('admin', '123456', '林总监（管理员）', 'ADMIN'),
('member', '123456', '苏晓（编导）', 'MEMBER');

-- 初始化团队成员
INSERT INTO team_member (name, position, department, phone, base_salary, status, join_date) VALUES
('林总监', '主理人/制片', '核心管理组', '13800138001', 12000.00, '在职', '2024-03-01'),
('苏晓', '资深编导', '内容策划组', '13800138002', 8500.00, '在职', '2024-05-15'),
('陈默', '主摄影师', '拍摄制作组', '13800138003', 9000.00, '在职', '2024-06-10'),
('周屿', '后期剪辑师', '拍摄制作组', '13800138004', 8000.00, '在职', '2024-08-20'),
('唐糖', '新媒体运营', '商务运营组', '13800138005', 7500.00, '在职', '2025-01-05'),
('陆远', '实习剪辑', '拍摄制作组', '13800138006', 4500.00, '休假', '2025-09-01');

-- 初始化设备台账
INSERT INTO team_equipment (asset_code, name, category, price, status, borrower_id, borrower_name, borrowed_at, remark) VALUES
('EQ-CAM-001', 'Sony A7S3 全画幅微单', '相机', 23999.00, '借出', 3, '陈默', '2026-11-10', '户外探店拍摄使用'),
('EQ-LEN-002', 'Sony FE 24-70mm F2.8 GM II', '镜头', 14499.00, '借出', 3, '陈默', '2026-11-10', '搭配A7S3使用'),
('EQ-MIC-003', 'DJI Mic 2 双人无线麦', '收音', 2499.00, '在库', NULL, '', '', '电量已充满'),
('EQ-LGT-004', '爱图仕 Amaran 200x S 常亮灯', '灯光', 2199.00, '在库', NULL, '', '', '影棚2号机位'),
('EQ-GMB-005', '大疆 RS 4 Pro 稳定器', '稳定器', 5499.00, '维修', NULL, '', '', '俯仰轴校准中'),
('EQ-MAC-006', 'MacBook Pro 16 M3 Max', '电脑', 26999.00, '借出', 4, '周屿', '2026-10-01', '4K剪辑主力机');

-- 初始化薪资记录
INSERT INTO team_salary (member_id, member_name, pay_month, base_amount, bonus_amount, allowance_amount, deduction_amount, total_amount, status, remark) VALUES
(1, '林总监', '2026-11', 12000.00, 3500.00, 500.00, 0.00, 16000.00, '已发放', '11月管理绩效达标'),
(2, '苏晓', '2026-11', 8500.00, 2200.00, 400.00, 0.00, 11100.00, '已发放', '爆款视频脚本激励'),
(3, '陈默', '2026-11', 9000.00, 1800.00, 600.00, 0.00, 11400.00, '待发放', '含外拍交通补贴'),
(4, '周屿', '2026-11', 8000.00, 1600.00, 300.00, 100.00, 9800.00, '待发放', '迟到扣款100'),
(5, '唐糖', '2026-11', 7500.00, 1500.00, 300.00, 0.00, 9300.00, '待发放', '双11商单转化奖励');

-- 初始化项目进度
INSERT INTO team_project (title, platform, owner_id, owner_name, stage, progress, due_date, remark) VALUES
('《AI时代独立创作者的一天》深度Vlog', 'B站', 2, '苏晓', '后期剪辑', 75, '2026-11-20', '粗剪已完成，待配乐调色'),
('百元桌面好物改造实测（商单）', '抖音', 3, '陈默', '拍摄录制', 45, '2026-11-18', '棚拍完成，补拍特写镜头'),
('冬季露营轻量化装备清单', '小红书', 5, '唐糖', '审片待发', 90, '2026-11-16', '封面与文案终审中'),
('年度相机横评：微单选购指南', 'B站', 3, '陈默', '脚本撰写', 25, '2026-11-28', '参数对比表整理中'),
('工作室新棚搭建全记录', '视频号', 4, '周屿', '已发布', 100, '2026-11-08', '播放量已破12万');
