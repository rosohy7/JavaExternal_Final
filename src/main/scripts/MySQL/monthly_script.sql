USE bankapp;
UPDATE `bank_account` INNER JOIN `credit_bank_account`
	ON `bank_account`.`bank_account_id` = `credit_bank_account`.`bank_account_id`
    SET `bank_account`.`balance` =
    `bank_account`.`balance`+`credit_bank_account`.`accrued_interest`,
    `credit_bank_account`.`accrued_interest` = 0
WHERE `bank_account`.`bank_account_id` > 0;

UPDATE `bank_account` INNER JOIN `deposit_bank_account`
	ON `bank_account`.`bank_account_id` = `deposit_bank_account`.`bank_account_id`
    SET `bank_account`.`balance` =
    `bank_account`.`balance`+`deposit_bank_account`.`accrued_interest`,
    `deposit_bank_account`.`accrued_interest` = 0
WHERE `bank_account`.`bank_account_id` > 0;