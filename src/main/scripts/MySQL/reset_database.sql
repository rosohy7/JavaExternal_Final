DROP DATABASE IF EXISTS bankapp;
CREATE DATABASE bankapp CHARSET 'utf8';
USE bankapp;

CREATE TABLE IF NOT EXISTS `user` (
	`user_id` bigint AUTO_INCREMENT PRIMARY KEY,
    `login` varchar(35) UNIQUE,
    INDEX(`login`),
    `password_hash` char(64) NOT NULL,
	`role` enum('USER','ADMIN') NOT NULL
    
);

CREATE TABLE IF NOT EXISTS `bank_account`(
	`bank_account_id` bigint AUTO_INCREMENT PRIMARY KEY,
    `expires` date NOT NULL,
    INDEX(`expires`),
    `account_number` char(16) NOT NULL UNIQUE,
    INDEX(`account_number`),
    `account_type` enum('CREDIT', 'DEPOSIT') NOT NULL,
	`balance`  DECIMAL(19, 4) NOT NULL DEFAULT 0,
	`confirmed` bool DEFAULT false,
    `latest_update` date,
    INDEX(`latest_update`),
    `user_id` bigint NOT NULL,
    FOREIGN KEY (`user_id`)
		REFERENCES `user`(`user_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `credit_bank_account`(
	`bank_account_id` bigint PRIMARY KEY,
    `credit_limit` DECIMAL(19, 4) NOT NULL CHECK(`credit_limit`>=0),
    `interest_rate` DECIMAL(10,6) NOT NULL CHECK(`interest_rate`>0),
    `accrued_interest` DECIMAL(19, 4) NOT NULL CHECK(`credit_limit`>=0),
    FOREIGN KEY (`bank_account_id`)
		REFERENCES `bank_account`(`bank_account_id`)
        ON UPDATE CASCADE ON DELETE CASCADE  
    
);
CREATE TABLE IF NOT EXISTS `deposit_bank_account`(
	`bank_account_id` bigint PRIMARY KEY,
    `interest_rate` DECIMAL(10,6) NOT NULL CHECK(`interest_rate`>0),
    `accrued_interest` DECIMAL(19, 4) NOT NULL CHECK(`credit_limit`>=0),
    FOREIGN KEY (`bank_account_id`)
		REFERENCES `bank_account`(`bank_account_id`)
        ON UPDATE CASCADE ON DELETE CASCADE  
    
);



CREATE TABLE IF NOT EXISTS `transaction`(
	`transaction_id` bigint AUTO_INCREMENT PRIMARY KEY,
    `time_stamp` datetime NOT NULL,
    INDEX(`time_stamp`),
    `balance_change` DECIMAL(19,4) NOT NULL,
    `bank_fee` DECIMAL(19,4) NOT NULL DEFAULT 0,
    `transaction_type` enum('TRANSFER_TO','TRANSFER_FROM','PAYMENT') NOT NULL,
    `bank_account_id` bigint NOT NULL,
    `pair_transaction_id` bigint,
    FOREIGN KEY(`pair_transaction_id`)
       REFERENCES `transaction`(`transaction_id`)
       ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (`bank_account_id`)
		REFERENCES `bank_account`(`bank_account_id`)
        ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS `transfer_to_transaction`(
	`transaction_id` bigint PRIMARY KEY,
    `sender_account_number` char(16),
    FOREIGN KEY(`sender_account_number`)
		REFERENCES `bank_account`(`account_number`)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY(`transaction_id`)
		REFERENCES `transaction`(`transaction_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `transfer_from_transaction`(
	`transaction_id` bigint PRIMARY KEY,
    `receiver_account_number` char(16),
    FOREIGN KEY(`receiver_account_number`)
		REFERENCES `bank_account`(`account_number`)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY(`transaction_id`)
		REFERENCES `transaction`(`transaction_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `payment_transaction`(
	`transaction_id` bigint PRIMARY KEY,
    `payment_number` varchar(30) NOT NULL,
    FOREIGN KEY(`transaction_id`)
		REFERENCES `transaction`(`transaction_id`)
        ON UPDATE CASCADE ON DELETE CASCADE
);
#SELECT * FROM `user`;
SELECT * FROM `credit_bank_account`;