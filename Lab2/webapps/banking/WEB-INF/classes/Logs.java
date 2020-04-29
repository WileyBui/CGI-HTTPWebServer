import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Logs {
    public Logs() {}
    
    // Appends each user's log contents to its corresponding user log file
    public void appendToLog(String username, String contents) throws IOException {
        return;
        // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        // Date currentDate = new Date();

        // String textToAppend = formatter.format(currentDate) + ": " + contents + ".\n";
        // FileOutputStream outputStream = new FileOutputStream("../webapps/banking/logs/" + username + ".log", true);
        // byte[] strToBytes = textToAppend.getBytes();
        // outputStream.write(strToBytes);
        // outputStream.close();
    }
}