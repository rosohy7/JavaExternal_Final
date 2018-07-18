package external.letiuka.persistence.entities;

import external.letiuka.modelviewcontroller.model.AccountType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Represents bank account table in a database.
 */

@Entity(name = "BankAccount")
@Table(name = "bank_account")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BankAccountEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    protected long id;

    @Column(name = "expires")
    protected Date expires;

    @Column(name = "account_number")
    protected String accountNumber;

    @Column(name = "account_type")
    protected AccountType type;

    @Column(name = "balance")
    protected double accountBalance;

    @Column(name = "confirmed")
    protected boolean confirmed;

    @Column(name = "latest_update")
    protected Date latestUpdate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    protected UserEntity user;

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @OneToMany(mappedBy = "bankAccount")

    protected List<TransactionEntity> transactions;

    public BankAccountEntity() {
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }


    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Date getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(Date latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
}
