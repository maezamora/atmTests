package transactions;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

import user.Accounts;


public class Transaction  {
    private int uid;
    private String accountNumber;
    private double amount;
    private double balance;
    private Date date;
    private String details;
    private String chequeNo;
    private double withdrawAmount;
    private double depositAmount;

    public Transaction(){}

    public Transaction(Date date, String details, String chequeNo, double withdrawAmount, double depositAmount){
        this.date = date;
        this.details = details;
        this.chequeNo = chequeNo;
        this.withdrawAmount = withdrawAmount;
        this.depositAmount = depositAmount;
    }

    public Transaction(int uid, String accountNumber){
        this.uid=uid;
        this.accountNumber=accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Date getDate() {
        return this.date;
    }

    public String getDateCreated(){
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try{
            Date transactionCreated = formatter.parse(formatter.format(date));
            return formatter.format(transactionCreated);
        }
        catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public double getAmount()
    {
        return this.amount;
    }

    public double getBalance(String accNo){
        ArrayList<Transaction> transactions = getTransactionHistoryArrayList(accNo);


        double bal = 0;
        Withdraw_Deposit w= new Withdraw_Deposit();
        //System.out.println(transactions);
        for(int i=0;i<transactions.size();i++){
            //System.out.println(transactions.get(i).getDepositAmount() + " " + transactions.get(i).getWithdrawAmount());
            bal += transactions.get(i).getDepositAmount();
            bal -= transactions.get(i).getWithdrawAmount();
        }
        this.balance = bal;
        return this.balance;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDetails() {
        return this.details;
    }

    public String getChequeNo() {
        return this.chequeNo;
    }

    public double getWithdrawAmount() {
        return this.withdrawAmount;
    }

    public double getDepositAmount() {
        return this.depositAmount;
    }


    public ArrayList<Transaction> getTransactionHistoryArrayList(String accNo){
        ArrayList<Transaction> allTransactions = new ArrayList<Transaction>();
        int index = 0;
        String line = "";

        try{
            BufferedReader br = new BufferedReader(new FileReader("data/transactions.csv"));
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(accNo.equals(data[0])){
                    String stringDate = data[1];
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                    try{
                        Date date = formatter.parse(stringDate);
                        //No withdrawal is made
                        if(Float.parseFloat(data[4]) == 0){
                            try{
                                allTransactions.add(new Transaction(date,data[2],data[3],0,Double.parseDouble(data[5])));
                            }catch (NumberFormatException e){

                            }

                        }
                        //No deposit is made
                        try{
                            if(Float.parseFloat(data[5]) == 0){
                                try{
                                    allTransactions.add(new Transaction(date,data[2],data[3],Double.parseDouble(data[4]),0));
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                }

                            }
                        }catch(ArrayIndexOutOfBoundsException e){
                            allTransactions.add(new Transaction(date,data[2],data[3],Double.parseDouble(data[4]),0));
                        }

                    }catch(ParseException e){
                        e.printStackTrace();
                    }


                }

            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        return allTransactions;
    }


    //Add deposited value into .csv file
    public boolean addDeposit(String accountNumber, LocalDate date,String CHQ,String Deposit){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("data/transactionsTest.csv", true));
            out.write("\n"+accountNumber + "," + date+",Deposit"+","+CHQ+","+ Deposit);
            out.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    //Add withdrawn value into .csv file
    public boolean addWithdraw(String accountNumber,LocalDate date,String CHQ,String Withdraw){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("data/transactions.csv", true));
            out.write("\n" + accountNumber + "," + date + ",Withdrawal,"  + CHQ + "," + Withdraw + ",");
            out.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }



    //For overseas transaction
    protected void transactionWrite(String accountNo, String date, String details, String chqNo, double withdrawalAmount, double depositAmount,double balanceAmount) {
        String withdraw = String.format("%.2f",withdrawalAmount);
        String deposit = String.format("%.2f",depositAmount);
        String balance = String.format("%.2f",balanceAmount);
        List<List<String>> rows = Arrays.asList(
                Arrays.asList(accountNo,date,details,"1234545l",withdraw,deposit,balance)
        );

        try {

            FileWriter csvWriter = new FileWriter("data/transactions.csv",true);

            for (List<String> rowData : rows) {
                csvWriter.append("\n");
                csvWriter.append(String.join(",", rowData));

            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get the specific account balance from accounts.csv
    public double getAccountBalance(String accountNumber) {
        Accounts acctNum = new Accounts();
        acctNum.readAccountBalance(accountNumber);
        return acctNum.getBalance();
    }

    public void updateBalance(double remainingBalance, String targetAccNum){
        File oldFile = new File("data/accounts.csv");
        fileEditor(oldFile, remainingBalance, targetAccNum);
    }

    private void fileEditor(File oldFile, double changedValue, String targetAccNum){
        String newAcctNum = String.valueOf(this.accountNumber);
        List<String> newLines = new ArrayList<>();
        String[] column;
        try{
            for (String line : Files.readAllLines(Paths.get(oldFile.toURI()), StandardCharsets.UTF_8)) {
                column = line.split(",");
                try{
                    if(column[1].equals(targetAccNum)){
                        //Replaces the whole line after uid because if we were to only use column[3] for first argument in line.replace(),
                        //if the balance eg. the number 0 is in the account number column eg. 5750009988, it will replace all the zeros
                        //in account num column with the new balance
                        newLines.add(line.replace(column[1] + "," + column[2] + "," + column[3], column[1] + ","
                                + column[2] + "," + changedValue));
                    }else{
                        newLines.add(line);
                    }
                }catch(ArrayIndexOutOfBoundsException e){

                }

            }
            Files.write(Paths.get(oldFile.toURI()), newLines, StandardCharsets.UTF_8);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getTransactionHistory(String AccNo) {
        DecimalFormat money = new DecimalFormat("'$'###,##0.00");
        System.out.println("Displaying Transaction History for Account Number: " + AccNo);
        System.out.println("Current Balance is " + money.format(getAccountBalance(AccNo)) + "\n");
        ArrayList<Transaction> al = getTransactionHistoryArrayList(AccNo);
        //Get last 30 transaction records
        if(al.size() > 30){
            for (int i = al.size()-1; i >= al.size() - 30; i--){
                Date date = al.get(i).getDate();
                //Convert date to localDate to get month, year value
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(localDate.getYear() == 2018 && localDate.getMonthValue() == 7){
                    System.out.println(date + "               " + al.get(i).getDetails() + "               " + money.format(al.get(i).getWithdrawAmount()) + "               " + money.format(al.get(i).getDepositAmount()));
                }
            }
        }else{
            for (int i = al.size()-1; i >= 0; i--){
                Date date = al.get(i).getDate();
                //Convert date to localDate to get month, year value
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                double withdrawAmt = al.get(i).getWithdrawAmount();
                double depositAmt = al.get(i).getDepositAmount();
                if(depositAmt == 0){
                    System.out.println(date + "               " + al.get(i).getDetails() + "               -" + money.format(withdrawAmt));
                }
                else{
                    System.out.println(date + "               " + al.get(i).getDetails() + "               +" + money.format(depositAmt));
                }
            }
        }
    }

}
