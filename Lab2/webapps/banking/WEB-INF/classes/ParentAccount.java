import java.io.*;
import java.util.*;

public class ParentAccount implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;
    private String username;
    private String usernameID;
    private List<UserAccount> subAccountList = new ArrayList<UserAccount>();

    public ParentAccount(String username) {
        this.username    = username;
        this.usernameID  = UUID.randomUUID().toString();
    }

    public String getUsername() {
        return this.username;
    }

    public String getUsernameID() {
        return this.usernameID;
    }

    public List<UserAccount> getSubAccounts() {
        return this.subAccountList;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsernameID(String username) {
        this.usernameID = username;
    }

    public void addSubAccount(UserAccount newAccount) {
        this.subAccountList.add(newAccount);
    }

    public int removeSubAccountByAccountID(String accountID) {
        // return -2 = error  : needs to withdraw all money out first
        // return -1 = error  : could not find account
        // return  0 = success: removed sub account
        for (UserAccount account : this.subAccountList) {
            if (account.getAccountID().equals(accountID)) {
                if (account.getBalance() > 0) {
                    return -2;
                }
                this.subAccountList.remove(account);
                return 0;
            }
        }
        return -1;
    }

}
