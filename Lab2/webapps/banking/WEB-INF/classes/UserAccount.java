import java.io.*;
import java.util.UUID;

public class UserAccount implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;
    private String usernameID;
    private String accountType;
    private String accountID;
    private Double balance;

    public UserAccount(String userID, String accountType, double balance) {
        this.usernameID  = userID;
        this.accountType = accountType;
        this.balance     = balance;
        this.accountID   = UUID.randomUUID().toString();
    }

    public UserAccount(String userID, String accountType, String balance) {
        this.usernameID  = userID;
        this.accountType = accountType;
        this.accountID   = UUID.randomUUID().toString();

        try { 
            this.balance = Double.parseDouble(balance);
        } catch (NumberFormatException e) {
            this.balance = -1.0;
        }
    }

    public String getUsernameID() {
        return this.usernameID;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public String getAccountID() {
        return this.accountID;
    }

    public Double getBalance() {
        return this.balance;
    }

    public String getBalanceString() {
        return "$" + String.format("%.2f", this.balance);
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
