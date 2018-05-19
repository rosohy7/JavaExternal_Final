USE bankapp;
UPDATE `bank_account` INNER JOIN `credit_bank_account`
	ON `bank_account`.`bank_account_id` = `credit_bank_account`.`bank_account_id`
    SET `credit_bank_account`.`accrued_interest` =
    IF(`bank_account`.`balance`>0,
    `credit_bank_account`.`accrued_interest`,
    `credit_bank_account`.`accrued_interest` +
    `bank_account`.`balance`*`credit_bank_account`.`interest_rate`/(100.0*365))
WHERE `bank_account`.`bank_account_id` > 0;

UPDATE `bank_account` INNER JOIN `deposit_bank_account`
	ON `bank_account`.`bank_account_id` = `deposit_bank_account`.`bank_account_id`
    SET `deposit_bank_account`.`accrued_interest` =
    `deposit_bank_account`.`accrued_interest` +
    `bank_account`.`balance`*`deposit_bank_account`.`interest_rate`/(100.0*365)
WHERE `bank_account`.`bank_account_id` > 0;