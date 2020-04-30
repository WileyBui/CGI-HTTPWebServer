import java.io.*;
import java.util.*;

public class CreateSubAccount implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;

    private String username;
    private String accountType;
    private Double initialAmount;

    public CreateSubAccount(String username, String accountType, String money) {
        this.username = username;
        this.accountType = accountType;

        // CONVERT $ from string to double.
        try { 
            this.initialAmount = Double.parseDouble(money);
        } catch (NumberFormatException e) {
            this.initialAmount = -1.0;
        }
    }

    public boolean isErrorCreatingSubAccount() {
        // ERROR CHECK: prevent user from doing Inspect Element from client side
        List<String> errors = new ArrayList<>();
        if (initialAmount < 0) {
            errors.add("The initial deposit amount you entered is incorrect.");
        }
        if (!(accountType.equals("checking-account") || accountType.equals("saving-account") || accountType.equals("brokerage-account"))) {
            errors.add("Account type must be checking, saving, or brokerage.");
        }

        return true;
        // if (initialAmount < 0) {
        //     errors.add("The initial deposit amount you entered is incorrect.");
        // }
        // if (!(accountType.equals("checking-account") || accountType.equals("saving-account") || accountType.equals("brokerage-account"))) {
        //     errors.add("Account type must be checking, saving, or brokerage.");
        // }
    }
}