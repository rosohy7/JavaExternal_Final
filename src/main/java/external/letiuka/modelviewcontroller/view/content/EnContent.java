package external.letiuka.modelviewcontroller.view.content;

import java.util.ListResourceBundle;

/**
 * Stores english content of views
 */
public class EnContent extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"login.title", "Log In Page"},
            {"login.button", "Log in"},
            {"login.password", "Password"},
            {"login.login-label", "Login"},

            {"navbar.login", "Log in"},
            {"navbar.home", "Home"},
            {"navbar.request-credit", "Request credit account"},
            {"navbar.request-deposit", "Request deposit account"},
            {"navbar.logout", "Log out"},
            {"navbar.see-user-accounts", "List user`s accounts"},
            {"navbar.see-unconfirmed", "List unconfirmed accounts"},
            {"navbar.signup", "Sign up"},
            {"navbar.deposit", "Deposit to account"},
            {"navbar.withdraw", "Withdraw from account"},


            {"deposit.title", "Deposit money to account"},
            {"deposit.account-number", "Account number"},
            {"deposit.amount", "Amount"},
            {"deposit.button", "Deposit"},

            {"withdraw.title", "Withdraw money from account"},
            {"withdraw.account-number", "Account number"},
            {"withdraw.amount", "Amount"},
            {"withdraw.button", "Withdraw"},

            {"user-accounts.title", "See all of user`s accounts"},
            {"user-accounts.login", "User`s login"},
            {"user-accounts.button", "See accounts"},

            {"signup.title", "Sign Up Page"},
            {"signup.button", "Sign up"},
            {"signup.password", "Password"},
            {"signup.re-password", "Repeat password"},
            {"signup.login", "Login"},

            {"register-deposit.title", "Request deposit account"},
            {"register-deposit.button", "Request"},

            {"register-credit.title", "Request credit account"},
            {"register-credit.limit", "Credit limit"},
            {"register-credit.button", "Request"},

            {"index.title", "This is an online banking website"},

            {"account.title", "Account Page"},
            {"account.account-number", "Account number"},
            {"account.account-type", "Account type"},
            {"account.limit", "Credit limit"},
            {"account.balance", "Balance"},
            {"account.holder", "Holder"},
            {"account.history", "History"},
            {"account.balance-change", "Balance change"},
            {"account.fee", "Bank fee"},
            {"account.sender-number", "Sender account number"},
            {"account.receiver-number", "Receiver account number"},
            {"account.timestamp", "Timestamp"},
            {"account.transfer-title", "Transfer money"},
            {"account.transfer-to", "Receiver account number"},
            {"account.transfer-amount", "Amount"},
            {"account.transfer-button", "Transfer"},

            {"unconfirmed.title", "Unconfirmed accounts list"},
            {"unconfirmed.account-number", "Account number"},
            {"unconfirmed.account-type", "Account type"},
            {"unconfirmed.limit", "Credit limit"},
            {"unconfirmed.confirm-button", "Confirm"},
            {"unconfirmed.deny-button", "Deny"},

            {"user-accounts.title", "All user`s accounts"},
            {"user-accounts.account-number", "Account number"},
            {"user-accounts.account-type", "Account type"},
            {"user-accounts.limit", "Credit limit"},
            {"user-accounts.balance", "Balance"},
            {"user-accounts.holder", "Holder"},
            {"user-accounts.interest-rate", "Interest rate"},
            {"user-accounts.accrued-interest", "Accrued interest"},
            {"user-accounts.expires", "Expires"}
    };
}
