package user;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Accounts {
    private DecimalFormat money = new DecimalFormat("'$'###,##0.00");
    private String accountName;
    private String accountNumber;
    private double balance;
    ArrayList<String> accountNameList = new ArrayList<String>();
    ArrayList<String> balanceAmountList = new ArrayList<String>();

    public Accounts() {

    }

    public Accounts(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public ArrayList<String> getAccountNameList() {
        return this.accountNameList;
    }

    public ArrayList<String> getBalanceList() {
        return this.balanceAmountList;
    }

    public ArrayList<String> getAllAccounts(String userId) {
        ArrayList<String> accountNum = new ArrayList<String>();
        String line = "";
        int lineNo = 0; //Dont read the first line of the csv file

        try {
            BufferedReader br = new BufferedReader(new FileReader("data/accounts.csv"));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (lineNo > 0) {
                    //Add only the account numbers that are associated with the given user id
                    if (userId.equals(data[0])) {
                        accountNum.add(data[1]);
                        this.accountNameList.add(data[2]);
                        this.balanceAmountList.add(data[3]);

                    }
                }
                lineNo++;
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accountNum;
    }

    public void accountSelection(String userId) {
        Scanner input = new Scanner(System.in);
        System.out.println("\nAccounts under you:");
        System.out.println("|   AccountsNo  |   Name    |   Balance |");
        ArrayList<String> userAccs = new ArrayList<String>();

        userAccs = getAllAccounts(userId);
        for (int i = 0; i < userAccs.size(); i++) {
            System.out.println((i+1) + ". " + userAccs.get(i)+"|"+getAccountNameList().get(i)+"|"+ getBalanceList().get(i));
        }

        System.out.println("===============================");
        do {
            try{
                System.out.print("Select an account to continue: ");
                int accSelection = input.nextInt();
                if (0 < accSelection  && accSelection <= userAccs.size()) {
                    setAccountNumber(userAccs.get(accSelection-1));
                    System.out.println("\nYou have selected account " + getAccountNumber() + " for this transaction.");
                    break;
                }
                else {
                    System.out.println("Invalid option! Please try again.");
                    input.nextLine();
                }
            }
            catch(InputMismatchException e) {
                System.out.println("Invalid input! Please try again.\n");
                input.nextLine();
            }
        }
        while (true);
    }

    public void readAccountBalance(String accountNumber) {
        String line = "";
        int lineNo = 0; //Dont read the first line of the csv file
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/accounts.csv"));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (lineNo > 0) {
                    //get balance of account number
                    try{
                        if (accountNumber.equals(data[1])) {
                            this.balance = Double.parseDouble(data[3]);
                        }
                    }catch(ArrayIndexOutOfBoundsException e){

                    }

                }
                lineNo++;
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getBalance() {
        return balance;
    }
}
