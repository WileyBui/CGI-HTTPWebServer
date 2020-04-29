import java.io.*;
import java.util.*;

public class Database implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;

    public Database() {
    }

    public boolean isUserExist(String username) {
        List<UserAccount> accountList = this.getAllUserObjects();

        for (UserAccount account : accountList) {
            if (account.getUsername().toLowerCase().equals(username.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public List<UserAccount> getAllUserObjects() {
        File database = new File("database.txt");

        // CHECKING if file has already created and user already exist
        List<UserAccount> accountList = new ArrayList<>();
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
}
