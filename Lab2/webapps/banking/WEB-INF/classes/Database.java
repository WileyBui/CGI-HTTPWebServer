import java.io.*;
import java.util.*;

public class Database implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;

    public Database() {}

    public List<UserAccount> getAllUserObjects() {
        File              database    = new File("../webapps/banking/SubAccountDatabase.txt");
        List<UserAccount> accountList = new ArrayList<>();

        // CHECKING if file has already created and user already exist
        try {
            if (!database.createNewFile()) {
                ObjectInputStream db = new ObjectInputStream(new FileInputStream(database));
                while(true){
                    try {
                        accountList.add((UserAccount)db.readObject());
                    } catch (Exception e) {
                        db.close();
                        break;
                    }
                }
            }
        } catch(IOException e) {}
        return accountList;
    }

    public UserAccount getUserObject(String usernameID) {
        List<UserAccount> accountList = this.getAllUserObjects();

        for (UserAccount account : accountList) {
            if (account.getUsernameID().toLowerCase().equals(usernameID.toLowerCase())) {
                return account;
            }
        }
        return null;
    }

    public boolean isUserExist(String username) {
        UserAccount user = this.getUserObject(username);
        return (user != null)? true: false;
    }
}
