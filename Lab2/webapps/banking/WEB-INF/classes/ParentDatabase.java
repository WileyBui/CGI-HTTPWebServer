import java.io.*;
import java.util.*;

public class ParentDatabase implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;

    public ParentDatabase() {}

    public List<ParentAccount> getAllParentObjects() {
        File                database    = new File("../webapps/banking/ParentDatabase.txt");
        List<ParentAccount> accountList = new ArrayList<>();

        // CHECKING if file has already created and user already exist
        try {
            if (!database.createNewFile()) {
                ObjectInputStream db = new ObjectInputStream(new FileInputStream(database));
                while(true){
                    try {
                        accountList.add((ParentAccount)db.readObject());
                    } catch (Exception e) {
                        db.close();
                        break;
                    }
                }
            }
        } catch(IOException e) {}
        return accountList;
    }

    public ParentAccount getParentObject(String username) {
        List<ParentAccount> accountList = this.getAllParentObjects();

        for (ParentAccount account : accountList) {
            if (account.getUsername().toLowerCase().equals(username.toLowerCase())) {
                return account;
            }
        }
        return null;
    }

    public ParentAccount getParentObjectByUsernameID(String usernameID) {
        List<ParentAccount> accountList = this.getAllParentObjects();

        for (ParentAccount account : accountList) {
            if (account.getUsernameID().equals(usernameID)) {
                return account;
            }
        }
        return null;
    }

    public String getParentID(String username) {
        ParentAccount user = this.getParentObject(username);
        return user.getUsernameID();
    }

    public boolean isParentExist(String username) {
        ParentAccount user = this.getParentObject(username);
        return (user != null)? true: false;
    }
}
