package transactions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.naming.LimitExceededException;

import exceptions.InsufficientFundsException;
import exceptions.TransferLimitException;
import misc.Settings;

public class OverseasTransfer extends Transfer{
    private DecimalFormat money = new DecimalFormat("###,##0.00");
    private double transferAmount;
    private double moneyConvert;
    private double balance;
    private double remainingBalance;
    private String details;
    private String currency;
    private String recipientAccount;
    private Transaction t;
    private Settings settings;
    private Scanner input = new Scanner(System.in);


    public OverseasTransfer(int uid, String accountNumber) {
        super(uid,accountNumber);
        this.settings= new Settings(uid);
        this.t=new Transaction(uid,accountNumber);
        this.balance = t.getAccountBalance(accountNumber);
        System.out.println("Your balance: SGD "+money.format(balance));
    }

    @Override
    public void transferOptions(int option){
        switch (option) {
            case 1: {
                getTransferAmount();
                if (transferSuccess()) {
                    inputRecipientAccount();
                    transferInfo("MYR",transferAmount,remainingBalance);
                }
                break;
            }
            case 2: {
                getTransferAmount();
                if (transferSuccess()) {
                    inputRecipientAccount();
                    transferInfo("IDR",transferAmount,remainingBalance);
                }
                break;
            }
            case 3: {
                getTransferAmount();
                if (transferSuccess()) {
                    inputRecipientAccount();
                    transferInfo("USD",transferAmount,remainingBalance);
                }
                break;
            }
            case 4: {
                getTransferAmount();
                if (transferSuccess()) {
                    inputRecipientAccount();
                    transferInfo("EUR",transferAmount,remainingBalance);
                }
                break;
            }
            default: System.out.println("Invalid option!");
        }
    }

    private void getTransferAmount(){
        settings.readSettings();
        do{
            try {
                System.out.print("Amount to transfer overseas: ");
                transferAmount=input.nextDouble();
                if(transferAmount<= 0) {
                    throw new IllegalArgumentException();
                }

                if(transferAmount< settings.getOverseasLimit() && transferAmount <= balance){
                    this.remainingBalance = balance -transferAmount;
                }
                else if (transferAmount > balance){
                    throw new InsufficientFundsException(transferAmount);
                }
                else if(transferAmount> settings.getOverseasLimit()){
                    throw new TransferLimitException(settings.getOverseasLimit());
                }
                break;
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("You have entered an invalid transfer amount!\n");
                input.nextLine();
            }
            catch (InsufficientFundsException e) {
                System.out.println("You are short of " + money.format((e.getAmount()-balance))+ " SGD\n");
            }
            catch (TransferLimitException e){
                System.out.println("This is more than your current overseas transfer limit.\nPlease increase your overseas transfer limit and try again!");
                System.out.println("Overseas transfer limit: SGD "+money.format(e.getLimit())+ "\n");
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
        if(transferAmount< settings.getOverseasLimit() && transferAmount <= balance){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void transferMoney(int recipientAccount,String currency,Double value) {
        this.currency=currency;
        Fx foreignExchange = new Fx();
        foreignExchange.getData(currency);
        System.out.println("1.00 SGD = "+money.format(foreignExchange.getBuyRate()) + " "+ currency);

        moneyConvert = value*foreignExchange.getBuyRate();
        details = "Overseas transfer of "+ String.format("%.2f",moneyConvert)+ currency +" to "+getRecipientAccountNum();
        setDetails(details);

        System.out.println(money.format(value)+" SGD = " + money.format(moneyConvert)+" "+currency+"\n");
        System.out.println("Successfully transferred " +money.format(moneyConvert)+ " " + currency + " to account number "+getRecipientAccountNum());
        System.out.println("Your remaining balance is " +money.format(remainingBalance)+ " SGD");
    }

}
