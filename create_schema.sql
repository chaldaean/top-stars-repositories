/*First run this script to create schema and user*/;
CREATE DATABASE IF NOT EXISTS `app`;
CREATE USER IF NOT EXISTS 'git'@'%' IDENTIFIED BY 'git';
GRANT ALL PRIVILEGES ON app.* TO 'git'@'%';
FLUSH PRIVILEGES;

