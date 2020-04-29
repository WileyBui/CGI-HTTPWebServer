import java.io.*;

public class UserAccount implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;
    private String id; // TODO
    private String username;
    private String accountType;
    private Double balance;

    public UserAccount() {
    }

    public UserAccount(String username, String accountType, double balance) {
        this.username    = username;
        this.accountType = accountType;
        this.balance     = balance;
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public Double getBalance() {
        return this.balance;
    }

    public String getBalanceString() {
        return "$" + String.format("%.2f", this.balance);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }
}
