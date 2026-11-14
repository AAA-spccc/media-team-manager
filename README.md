# 7A 自媒体团队管理系统（JavaEE 期末大作业）

基于 **Java 8 + Servlet 4.0 + JSP / JSTL + JDBC + MySQL 8.0** 构建的自媒体团队一体化管理后台，覆盖 **人员、设备、薪资、项目进度** 四大核心模块。

## 功能模块

1. **登录与权限过滤（`AuthFilter` / `LoginServlet`）**
   - 统一 UTF-8 编码与 Session 登录拦截
   - 演示账号：`admin / 123456`（管理员）、`member / 123456`（普通成员）
2. **团队总览看板（`DashboardServlet`）**
   - 实时汇总在职成员数、设备在库/借出数、累计薪资核算总额、进行中项目数与近期项目进度表
3. **人员管理（`MemberServlet`）**
   - 团队成员新增、编辑、删除，支持按姓名/岗位/小组关键字及在职状态筛选
4. **设备台账与借还（`EquipmentServlet`）**
   - 摄影/灯光/收音/稳定器/电脑等设备入库，支持一键**借出（关联成员） / 归还 / 报修**及分类筛选
5. **薪资核算与发放（`SalaryServlet`）**
   - 自动读取成员底薪，按 `实发 = 基本工资 + 绩效奖金 + 补贴 - 扣款` 生成月度工资单，支持按月筛选与一键标记“已发放”
6. **项目进度管理（`ProjectServlet`）**
   - 面向抖音/B站/小红书/视频号等内容项目的全阶段跟踪（`选题策划 → 脚本撰写 → 拍摄录制 → 后期剪辑 → 审片待发 → 已发布`），支持实时更新阶段与百分比进度条

## 快速运行指南

1. **导入数据库**
   - 启动本地 MySQL 8.0 服务，执行 [schema.sql](sql/schema.sql) 创建 `media_team_db` 数据库及初始演示数据。
   - 按本机密码修改 [db.properties](src/main/resources/db.properties) 中的 `db.username` 与 `db.password`。
2. **IDE 导入与构建**
   - 使用 IntelliJ IDEA 打开本目录（Maven 项目），等待索引加载完成（所需依赖已在本地 `.m2` 仓库）。
   - 在 IDEA 中配置 **Tomcat 8.5 / 9.0**，添加 `media-team-manager:war exploded` 工件。
3. **启动访问**
   - 启动 Tomcat 后访问 `http://localhost:8080/media-team-manager/login`（或根路径）。
