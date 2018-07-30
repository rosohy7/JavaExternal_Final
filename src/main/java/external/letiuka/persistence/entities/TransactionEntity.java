package external.letiuka.persistence.entities;

import external.letiuka.modelviewcontroller.model.TransactionType;
import org.hibernate.annotations.Cascade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
/**
 * Responsible for bank transaction table in a database.
 */
@Entity(name = "Transaction")
@Table(name = "transaction",
    indexes = {@Index(columnList = "time_stamp")})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    protected long id;

    @Column(name = "time_stamp")
    protected Timestamp timeStamp;

    @Column(name = "balance_change")
    protected double balanceChange;

    @Column(name = "bank_fee")
    protected double bankFee;

    @Column(name = "transaction_type")
    protected TransactionType type;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    protected BankAccountEntity bankAccount;

    @OneToOne
    @JoinColumn(name = "pair_transaction_id")
    protected TransactionEntity pair;

    public TransactionEntity() {
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getBalanceChange() {
        return balanceChange;
    }

    public void setBalanceChange(double balanceChange) {
        this.balanceChange = balanceChange;
    }

    public double getBankFee() {
        return bankFee;
    }

    public void setBankFee(double bankFee) {
        this.bankFee = bankFee;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountEntity bankAccount) {
        this.bankAccount = bankAccount;
    }

    public TransactionEntity getPair() {
        return pair;
    }

    public void setPair(TransactionEntity pair) {
        this.pair = pair;
    }
}
