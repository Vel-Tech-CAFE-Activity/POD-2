create database fileupload;

use fileupload;

CREATE TABLE files
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      varchar(100),
    hash      varchar(250),
    file_data JSON
);

