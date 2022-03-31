package transactions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.naming.InsufficientResourcesException;

import exceptions.InsufficientFundsException;
public class Withdraw_Deposit extends Transaction{
    private DecimalFormat money = new DecimalFormat("'$'###,##0.00");

    public Withdraw_Deposit(){

    }

    public Withdraw_Deposit(int uid, String accountNumber){
        super(uid, accountNumber);
    }

    Scanner sc= new Scanner(System.in);
    public void depositMoney(double balance)
    {
        do{
            try{
                System.out.print("Enter Amount to deposit: $");
                float deposit=sc.nextFloat();
                if (deposit <= 0) {
                    throw new IllegalArgumentException();
                }
                balance= balance + deposit;
                System.out.println("Successfully deposited amount of "+money.format(deposit));
                System.out.println("Balance left in account "+money.format(balance));
                transactionWrite(getAccountNumber(),getDateCreated(),"Deposit",null,0.00,deposit,balance);
                updateBalance(balance, getAccountNumber());
                break;
            }
            catch (IllegalArgumentException e) {
                System.out.println("You have entered an invalid deposit amount!\n");
                sc.nextLine();
            }
            catch (InputMismatchException ime) {
                System.out.println("Invalid input! Please try again.\n");
                sc.nextLine();
            }
        }
        while(true);
    }

    public void withdrawMoney(double balance)
    {
        do{
            try {
                System.out.print("Enter Amount to withdraw: $");
                double withdraw=sc.nextDouble();
                if (withdraw <= 0) {
                    throw new IllegalArgumentException();
                }

                System.out.println("You are withdrawing " + money.format(withdraw) + " Balance is " + money.format(balance));
                if (withdraw<=balance)
                {
                    balance= balance - withdraw;
                    System.out.println("Successfully withdrawn amount of "+money.format(withdraw));
                    System.out.println("Balance left in account "+money.format(balance));
                    transactionWrite(getAccountNumber(),getDateCreated(),"Withdraw",null,withdraw,0.00,balance);
                    updateBalance(balance, getAccountNumber());
                }
                else{
                    throw new InsufficientFundsException(withdraw);
                }
                break;
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("You have entered an invalid deposit amount!\n");
                sc.nextLine();
            }
            catch (InsufficientFundsException e) {
                System.out.println("You are short of $" + (e.getAmount()-balance) + "\n");
                sc.nextLine();
            }
            catch (InputMismatchException ime) {
                System.out.println("Invalid input! Please try again.\n");
                sc.nextLine();
            }
        }
        while(true);
    }
}





