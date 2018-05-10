DROP DATABASE IF EXISTS bankapp;
CREATE DATABASE bankapp CHARSET 'utf8';
USE bankapp;

CREATE TABLE IF NOT EXISTS `user` (
	`user_id` int AUTO_INCREMENT PRIMARY KEY,
	`role` enum('USER','ADMIN') NOT NULL
);

CREATE TABLE IF NOT EXISTS `authentication` (
	`authentication_id` int AUTO_INCREMENT PRIMARY KEY,
    `login` varchar(35) UNIQUE,
    `password` varchar(100) NOT NULL,
    `salt` varchar(100),
    `user_id` int NOT NULL,
    FOREIGN KEY (`user_id`)
		REFERENCES `user`(`user_id`)
        ON UPDATE CASCADE ON DELETE CASCADE    
);

CREATE TABLE IF NOT EXISTS `bank_account`(
	`bank_account_id` int AUTO_INCREMENT PRIMARY KEY,
    `expires` date NOT NULL,
    `account_number` char(16) NOT NULL UNIQUE,
    `accout_type` enum('DEBIT', 'CREDIT', 'DEPOSIT') NOT NULL,
	`account_balance`  DECIMAL(19, 4) NOT NULL DEFAULT 0,
	`confirmed` bool DEFAULT false,
    `user_id` int NOT NULL,
    FOREIGN KEY (`user_id`)
		REFERENCES `user`(`user_id`)
        ON UPDATE CASCADE ON DELETE CASCADE  
    
);

CREATE TABLE IF NOT EXISTS `credit_bank_account`(
	`bank_account_id` int PRIMARY KEY,
    `credit_limit` DECIMAL(19, 4) NOT NULL CHECK(`credit_limit`>=0),
    `interest_rate` DECIMAL(10,6) NOT NULL CHECK(`interest_rate`>0),
    `accrued_interest` DECIMAL(19, 4) NOT NULL CHECK(`credit_limit`>=0),
    FOREIGN KEY (`bank_account_id`)
		REFERENCES `bank_account`(`bank_account_id`)
        ON UPDATE CASCADE ON DELETE CASCADE  
    
);
CREATE TABLE IF NOT EXISTS `deposit_bank_account`(
	`bank_account_id` int PRIMARY KEY,
    `interest_rate` DECIMAL(10,6) NOT NULL CHECK(`interest_rate`>0),
    `accrued_interest` DECIMAL(19, 4) NOT NULL CHECK(`credit_limit`>=0),
    FOREIGN KEY (`bank_account_id`)
		REFERENCES `bank_account`(`bank_account_id`)
        ON UPDATE CASCADE ON DELETE CASCADE  
    
);


CREATE TABLE IF NOT EXISTS `credentials` (
	`credentials_id` int AUTO_INCREMENT PRIMARY KEY,
	`first_name` varchar(35) NOT NULL,
    `middle_name` varchar(35),
    `last_name` varchar(35) NOT NULL,
    `date_of_birth` date NOT NULL,
    `user_id` int NOT NULL,
    FOREIGN KEY (`user_id`)
		REFERENCES `user`(`user_id`)
        ON UPDATE CASCADE ON DELETE CASCADE      
);

CREATE TABLE IF NOT EXISTS `transaction`(
	`transaction_id` int AUTO_INCREMENT PRIMARY KEY,
    `time_stamp` timestamp NOT NULL,    
    `account_balance_change` DECIMAL(19,4) NOT NULL,
    `bank_fee` DECIMAL(19,4) NOT NULL DEFAULT 0,
    `transaction_type` enum('TRANSFER_TO','TRANSFER_FROM','PAYMENT') NOT NULL,
    `bank_account_id` int NOT NULL,
    FOREIGN KEY (`bank_account_id`)
		REFERENCES `bank_account`(`bank_account_id`)
        ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS `payment_transaction`(
	`transaction_id` int PRIMARY KEY,
    `payment_number` varchar(30) NOT NULL,
    FOREIGN KEY(`transaction_id`)
		REFERENCES `transaction`(`transaction_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `transfer_to_transaction`(
	`transaction_id` int PRIMARY KEY,
    `sender_account_number` char(16) NOT NULL,
    FOREIGN KEY(`transaction_id`)
		REFERENCES `transaction`(`transaction_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `transfer_from_transaction`(
	`transaction_id` int PRIMARY KEY,
    `receiver_account_number` char(16) NOT NULL,
    FOREIGN KEY(`transaction_id`)
		REFERENCES `transaction`(`transaction_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);
#SELECT * FROM `user`;
