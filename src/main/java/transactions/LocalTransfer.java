package transactions;
import misc.*;
import user.*;
import exceptions.*;

import javax.naming.LimitExceededException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LocalTransfer extends Transfer{
    private DecimalFormat money = new DecimalFormat("'$'###,##0.00");
    private Scanner input = new Scanner(System.in);
    private Settings settings;
    private Transaction t;
    private Accounts userAcct = new Accounts();
    private String myOtherAccount;
    private double transferAmount;
    private double remainingBalance;
    private double balance;
    private String details;

    public LocalTransfer(int uid, String accountNumber){
        super(uid,accountNumber);
        this.settings= new Settings(uid);
        this.t=new Transaction(uid,accountNumber);
        this.balance = t.getAccountBalance(accountNumber);
    }

    @Override
    public void transferOptions(int option) {
        switch (option){
            case 1:
                System.out.println("Local Recipients");
                getTransferAmount();
                if(transferSuccess()){
                    inputRecipientAccount();
                    transferInfo("SGD",transferAmount,remainingBalance);
                }
                break;
            case 2:
                getUserAccounts();
                getTransferAmount();
                if (transferSuccess()){
                    myAccountsTransferInfo(myOtherAccount,transferAmount,remainingBalance);
                }
                break;
            default:
                System.out.println("Invalid Option!");
                break;
        }
    }

    @Override
    public void transferMoney(int recipientAccount, String currency, Double value) {
        details = "Local transfer to "+getRecipientAccountNum();
        setDetails(details);

        System.out.println("Successfully transferred " +money.format(transferAmount)+ " to account number "+getRecipientAccountNum());
        System.out.println("Your remaining balance is " +money.format(remainingBalance));
    }

    private void getTransferAmount(){
        settings.readSettings();
        do{
            try {
                System.out.print("Amount to transfer: $");
                transferAmount=input.nextDouble();
                if(transferAmount<= 0) {
                    throw new IllegalArgumentException();
                }

                if(transferAmount< settings.getTransferLimit() && transferAmount <= balance){
                    this.remainingBalance = balance -transferAmount;
                }
                else if (transferAmount > balance){
                    throw new InsufficientFundsException(transferAmount);
                }
                else if(transferAmount> settings.getTransferLimit()){
                    throw new TransferLimitException(settings.getTransferLimit());
                }
                break;
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("You have entered an invalid transfer amount!\n");
                input.nextLine();
            }
            catch (InsufficientFundsException e) {
                System.out.println("You are short of " + money.format((e.getAmount()-balance))+ "\n");
            }
            catch (TransferLimitException e){
                System.out.println("This is more than your current local transfer limit.\nPlease increase your local transfer limit and try again!");
                System.out.println("Local transfer limit: "+money.format(e.getLimit())+ "\n");
            }
            catch (InputMismatchException ime) {
                System.out.println("Invalid input! Please try again.\n");
                input.nextLine();
            }
        }while (true);
    }

    @Override
    public boolean transferSuccess() {
        settings.readSettings();
        if(transferAmount< settings.getTransferLimit() && transferAmount <= balance){
            return true;
        }
        else{
            return false;
        }
    }

    private void getUserAccounts(){
        System.out.println("Accounts under you: ");
        ArrayList<String> userAccs = new ArrayList<String>();
        userAccs = userAcct.getAllAccounts(getUid() + "");
        for (int i = 0; i < userAccs.size(); i++) {
            System.out.println((i+1) + ". " + userAccs.get(i));
        }

        System.out.println("===============================");
        System.out.print("Select an account to continue: ");
        int accSelection = input.nextInt();
        if (0 < accSelection  && accSelection <= userAccs.size()) {
            userAcct.setAccountNumber(userAccs.get(accSelection-1));
            this.myOtherAccount=userAcct.getAccountNumber();
            System.out.println("You have selected account " + userAcct.getAccountNumber() + " for this transaction.\n");
        }
        else {
            System.out.println("Invalid option!");
        }

    }

    public void myAccountsTransferInfo(String myOtherAccount, double transferAmount, double remainingBalance){
        String details1 = "Local transfer your account number " + myOtherAccount;
        t.transactionWrite(getAccountNumber(),t.getDateCreated(),details1,null,transferAmount,0.00,remainingBalance);
        t.updateBalance(remainingBalance, getAccountNumber());

        //The code below updates the other account
        double newBalance = transferAmount + t.getAccountBalance(myOtherAccount); //The other account's new balance after receiving money
        String details2 = "Received " + money.format(transferAmount) + " from " + getAccountNumber();
        t.transactionWrite(myOtherAccount, t.getDateCreated(), details2, null, 0.00, transferAmount, newBalance);
        t.updateBalance(newBalance, myOtherAccount);

        System.out.println("Successfully transferred " +money.format(transferAmount)+ " to your account number "+myOtherAccount);
        System.out.println("Your remaining balance is " +money.format(remainingBalance));
    }



}
