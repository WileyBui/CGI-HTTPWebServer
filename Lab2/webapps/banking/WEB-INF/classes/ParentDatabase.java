import java.io.*;
import java.util.*;

public class ParentDatabase implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;

    public ParentDatabase() {}

    public List<ParentAccount> getAllUserObjects() {
        File              database    = new File("../webapps/banking/ParentDatabase.txt");
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

    public ParentAccount getParentObject(String usernameID) {
        List<ParentAccount> accountList = this.getAllUserObjects();

        for (ParentAccount account : accountList) {
            if (account.getUsernameID().toLowerCase().equals(usernameID.toLowerCase())) {
                return account;
            }
        }
        return null;
    }

    public boolean isParentExist(String username) {
        ParentAccount user = this.getParentObject(username);
        return (user != null)? true: false;
    }
}
