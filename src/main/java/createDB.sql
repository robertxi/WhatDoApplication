CREATE USER wdadmin IDENTIFIED BY 'wdpwd';
-- DROP DATABASE test;
CREATE DATABASE whatdodb DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
GRANT ALL ON whatdodb.* TO 'wdadmin'@'localhost' IDENTIFIED BY 'wdpwd';
GRANT ALL ON whatdodb.* TO 'wdadmin'@'%' IDENTIFIED BY 'wdpwd';
FLUSH PRIVILEGES;