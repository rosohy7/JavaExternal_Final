package external.letiuka.modelviewcontroller.view.content;

import java.util.ListResourceBundle;
/**
 * Stores russian content of views
 */
public class RuContent extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            { "login.title", "Страница входа" },
            { "login.button", "Войти" },
            { "login.password", "Пароль" },
            { "login.login-label", "Логин" },

            {"navbar.login","Войти"},
            {"navbar.home", "Главная страница"},
            {"navbar.request-credit", "Запросить кредитный счет"},
            {"navbar.request-deposit", "Запросить депозитный счет"},
            {"navbar.logout", "Выйти"},
            {"navbar.see-user-accounts", "Показать счета пользователя"},
            {"navbar.see-unconfirmed", "Показать неподтверждённые счета"},
            {"navbar.signup", "Зарегистрироваться"},
            {"navbar.deposit", "Положить на счет"},
            {"navbar.withdraw", "Снять со счета"},


            {"deposit.title", "Положить деньги на счёт"},
            {"deposit.account-number", "Номер счёта"},
            {"deposit.amount", "Количество денег"},
            {"deposit.button", "Положить"},

            {"withdraw.title", "Снять деньги со счёта"},
            {"withdraw.account-number", "Номер счёта"},
            {"withdraw.amount", "Количество денег"},
            {"withdraw.button", "Снять"},

            {"user-accounts.title", "Показать все счета пользователя"},
            {"user-accounts.login", "Логин пользователя"},
            {"user-accounts.button", "Показать"},

            {"signup.title", "Страница регистрации"},
            {"signup.button", "Зарегистрироваться"},
            {"signup.password", "Пароль"},
            {"signup.re-password", "Повторите пароль"},
            {"signup.login", "Логин"},

            {"register-deposit.title", "Запросить депозитный счет"},
            {"register-deposit.button", "Запросить"},

            {"register-credit.title", "Запросить кредитный счет"},
            {"register-credit.limit", "Кредитный лимит"},
            {"register-credit.button", "Запросить"},

            {"index.title", "Это сайт онлайн-банкинга"},

            {"account.title", "Страница счёта"},
            {"account.account-number", "Номер счёта"},
            {"account.account-type", "Тип счёта"},
            {"account.limit", "Кредитный лимит"},
            {"account.balance", "Баланс"},
            {"account.holder", "Владелец"},
            {"account.history", "История транзакций"},
            {"account.balance-change", "Изменение баланса"},
            {"account.fee", "Комиссия"},
            {"account.sender-number", "Номер счёта-отправителя"},
            {"account.receiver-number", "Номер счёта-получателя"},
            {"account.timestamp", "Дата и время"},
            {"account.transfer-title", "Отправить деньги"},
            {"account.transfer-to", "Номер счёта-получателя"},
            {"account.transfer-amount", "Количество денег"},
            {"account.transfer-button", "Отправить"},

            {"unconfirmed.title", "Список неподтверждённых счетов"},
            {"unconfirmed.account-number", "Номер счёта"},
            {"unconfirmed.account-type", "Тип счёта"},
            {"unconfirmed.limit", "Кредитный лимит"},
            {"unconfirmed.confirm-button", "Подвтердить"},
            {"unconfirmed.deny-button", "Отказать"},

            {"user-accounts.title", "Все счета пользователя"},
            {"user-accounts.account-number", "Номер счёта"},
            {"user-accounts.account-type", "Тип счёта"},
            {"user-accounts.limit", "Кредитный лимит"},
            {"user-accounts.balance", "Баланс"},
            {"user-accounts.holder", "Владелец"},
            {"user-accounts.interest-rate", "Проценты"},
            {"user-accounts.accrued-interest", "Накопленые проценты"},
            {"user-accounts.expires", "Истекает"}
    };
}
