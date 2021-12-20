/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */




/**
 * The sql is run in MySQLWorkbench 
 * this is just a file used to save the sql code for safe keeping
 *
 *
 *    
 * Author:  Sébastien Malmberg
 * Created: 20 Dec 2021
 */
CREATE SCHEMA test;

USE test;

DROP TABLE results;
DROP TABLE selector;
DROP TABLE questions;
DROP TABLE users;
DROP TABLE quizzes;


CREATE TABLE IF NOT EXISTS users(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        firstName VARCHAR(32) NOT NULL,
        lastName  VARCHAR(32) NOT NULL,
        email	  VARCHAR(32) UNIQUE NOT NULL,
        username  VARCHAR(32) UNIQUE NOT NULL,
	password  VARCHAR(32) NOT NULL
);

INSERT INTO users (firstName, lastName, email, username, password) VALUES ('Steve', 'Stevenson', 'example@example.com', 'stevie', md5('password'));

SELECT * FROM users;