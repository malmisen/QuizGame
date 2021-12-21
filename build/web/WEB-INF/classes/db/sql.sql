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
DROP TABLE alternative;
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

SELECT * FROM users WHERE username = 'stevie';

SELECT * FROM users;

CREATE TABLE IF NOT EXISTS alternative(
	id INT AUTO_INCREMENT PRIMARY KEY,
        text VARCHAR(255) NOT NULL,
        question_id INT NOT NULL,
        quiz_id INT NOT NULL,
        FOREIGN KEY (question_id) REFERENCES questions(id),
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

INSERT INTO alternative (text, question_id, quiz_id) VALUES ("Linux", 1, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("Windows", 1, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("MacOS", 1, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("3", 2, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("10", 2, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("24", 2, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("Good", 3, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("Bad", 3, 1);
INSERT INTO alternative (text, question_id, quiz_id) VALUES ("Ugly", 3, 1);

CREATE TABLE IF NOT EXISTS questions(
	id      INT AUTO_INCREMENT PRIMARY KEY,
        text    VARCHAR(255) NOT NULL,
        quiz_id INT NOT NULL,
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

INSERT INTO questions (text, quiz_id) VALUES ("What is my name?", 1);
INSERT INTO questions (text, quiz_id) VALUES ("How old am I?", 1);
INSERT INTO questions (text, quiz_id) VALUES ("How do you do?", 1);

SELECT * FROM questions;

CREATE TABLE IF NOT EXISTS quizzes(
	id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	category    VARCHAR(32) NOT NULL,
        difficulty  VARCHAR(32) NOT NULL
);

INSERT INTO quizzes (category, difficulty) VALUES ("linux", "easy");
SELECT * FROM quizzes;

CREATE TABLE IF NOT EXISTS results(
	id              INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        quizCategory    VARCHAR(32) NOT NULL,
        quizDifficulty  VARCHAR(32) NOT NULL,
        score           INT NOT NULL,
        user_id         INT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id)
);

SELECT q.text FROM questions AS q WHERE q.quiz_id = 1;
SELECT a.text FROM alternative AS a where a.quiz_id = 1 AND a.question_id = 1;