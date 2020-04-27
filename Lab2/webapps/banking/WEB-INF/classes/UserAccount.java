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
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountType() {
        return accountType;
    }

    public Double getBalance() {
        return balance;
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
}
