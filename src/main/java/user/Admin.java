package user;
import java.io.*;
import java.util.*;

public class Admin {
    //View all existing accounts in the bank
    public ArrayList<Accounts> viewAllAccounts(){
        ArrayList<Accounts> allAccounts = new ArrayList<Accounts>();
        String line = "";
        int lineNo = 0; //Dont read the first line of the csv file

        try{
            BufferedReader br = new BufferedReader(new FileReader("data/accounts.csv"));
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(lineNo > 0){
                    try{
                        allAccounts.add(new Accounts(data[1],data[2]));
                    }catch(ArrayIndexOutOfBoundsException e){
                        //If there's an empty record in accounts.csv, skip it and continue adding the next account into the arrayList
                    }
                }
                lineNo++;
            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return allAccounts;
    }

    //Create new accounts for the customer
    public boolean createAccount(String userId, String accountNumber, String accountName){
        try{
            //Appends properly only if the last line of the file is not empty. If the last line of the file is empty,
            //It will write on the next line, leaving the previous line empty.
            BufferedWriter out = new BufferedWriter(new FileWriter("data/accounts.csv", true));
            out.write("\n" + userId + "," + accountNumber + "," + accountName + "," + "0");
            out.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
