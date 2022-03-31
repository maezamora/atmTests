import java.text.DecimalFormat;
import transactions.*;
import user.*;
import misc.*;
import java.io.*;
import java.util.*;

public class UserPrompt {
    private int uid;

    public UserPrompt(int uid){
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }


    // Continuation Prompt
    private void contPrompt() {
        Scanner prompt = new Scanner(System.in);

        do{
            System.out.print("\nWould you like to continue with a new transaction? [Y/N]: ");
            String userInput = prompt.nextLine();
            if(userInput.equalsIgnoreCase("Y") || userInput.equalsIgnoreCase("Yes"))
            {
                System.out.println("\n==== Continuing with session... ====\n");
                option(getUid()+"");
                break;
            }
            else if(userInput.equalsIgnoreCase("N") || userInput.equalsIgnoreCase("No"))
            {
                System.out.println("Thank you for transacting with us.\nHave a nice day!");
                break;
            }
            else {
                System.out.println("Invalid Input. Please try again.");
            }
        }
        while(true);
    }

    //This method is to display UI for the user
    public void option(String userId){
        DecimalFormat money = new DecimalFormat("'$'###,##0.00");
        Transaction t = new Transaction();
        Accounts acc = new Accounts();
        //Scanner input = new Scanner(System.in);
        Scanner options = new Scanner(System.in);
        int option, choice;

        // Prompt user to select an account to transact
        acc.accountSelection(userId + "");

        // Display balance for account
        System.out.println("Current Balance is " + money.format(t.getAccountBalance(acc.getAccountNumber())) + "\n");
        // Set userAcct
        String userAcct = acc.getAccountNumber();


        // Main transaction of program
        System.out.println("\nHello! What would you like to do today?");
        System.out.println("1. Withdraw\n2. Deposit\n3. Transfer\n4. View Transaction History\n5. Settings\n6. Logout");


        do{
            try{
                System.out.print("Enter your option: ");
                option = options.nextInt();
                if (1 <= option && option <= 6)
                {
                    break;
                }
                else {
                    System.out.println("Invalid option! Please try again.\n");
                    options.nextLine();
                }
            }
            catch(InputMismatchException e) {
                System.out.println("Invalid input! Please try again.\n");
                options.nextLine();
            }
        }
        while (true);

        switch (option){
            case 1:
                System.out.println("\n====== Withdrawal ======");

                //Needs try catch because in Withdraw_Deposit, .withdrawMoney() throws InsufficientResourcesException
                // try{
                Withdraw_Deposit withdraw = new Withdraw_Deposit(this.uid,userAcct);
                System.out.println("Your current balance is " + money.format(t.getAccountBalance(userAcct)));
                withdraw.withdrawMoney(t.getAccountBalance(userAcct)); //Needs to input 2 argument. First argument is amount to withdraw, second argument is balance of the account
                //LocalDate localDate = LocalDate.now();
                //t.addWithdraw(userAcct,localDate,"",withdraw);

                // }catch(InsufficientResourcesException e){
                //     e.printStackTrace();
                // }
                contPrompt();
                break;
            case 2:
                System.out.println("\n====== Deposit ======");
                Withdraw_Deposit deposit = new Withdraw_Deposit(this.uid,userAcct);
                System.out.println("Your current balance is " + money.format(t.getAccountBalance(userAcct)));
                deposit.depositMoney(t.getAccountBalance(userAcct));
                // LocalDate localDate = LocalDate.now();
                // t.addDeposit(userAcct,localDate,"",""); //cannot go to next line
                contPrompt();
                break;
            case 3:
                System.out.println("\n====== Transfer ======");
                System.out.println("1. Local Transfer\n2. Overseas Transfer");
                do {
                    try{
                        System.out.print("Enter your option: ");
                        choice = options.nextInt();
                        if (choice != 1 && choice !=2) {
                            System.out.println("Invalid option! Please try again.\n");
                            options.nextLine();
                        }
                        else {
                            break;
                        }
                    }
                    catch (InputMismatchException ime) {
                        System.out.println("Invalid input! Please try again.\n");
                        options.nextLine();
                    }
                }
                while(true);
                switch(choice) {
                    case 1:
                        int localOption, countriesOption;
                        System.out.println("\n====== Local Transfer ======");
                        LocalTransfer singapore = new LocalTransfer(this.uid,userAcct);
                        System.out.println("1.Local Recipients\n2.Between my accounts");
                        do {
                            try{
                                System.out.print("Enter your option: ");
                                localOption = options.nextInt();
                                if (localOption != 1 && localOption !=2) {
                                    System.out.println("Invalid option! Please try again.\n");
                                    options.nextLine();
                                }
                                else {
                                    break;
                                }
                            }
                            catch (InputMismatchException ime) {
                                System.out.println("Invalid input! Please try again.\n");
                                options.nextLine();
                            }
                        }
                        while (true);
                        singapore.transferOptions(localOption);

                        break;

                    case 2:
                        System.out.println("\n====== Overseas Transfer ======");
                        OverseasTransfer overseas = new OverseasTransfer(this.uid,userAcct);
                        System.out.println("1. Malaysia\n2. Indonesia\n3. United States\n4. Europe\n");
                        do {
                            try{
                                System.out.print("Enter your option: ");
                                countriesOption = options.nextInt();
                                if (countriesOption < 1 && countriesOption > 4) {
                                    System.out.println("Invalid option! Please try again.\n");
                                    options.nextLine();
                                }
                                else {
                                    break;
                                }
                            }
                            catch (InputMismatchException ime) {
                                System.out.println("Invalid input! Please try again.\n");
                                options.nextLine();
                            }
                        }
                        while (true);
                        overseas.transferOptions(countriesOption);
                        break;
                }
                contPrompt();
                break;
            case 4:
                System.out.println("\n====== Transaction History ======");
                t.getTransactionHistory(userAcct);
                contPrompt();
                break;

            case 5:
                System.out.println("\n====== Settings ======");
                System.out.println("1. Set Local Transfer Limit\n2.Set Overseas Transfer Limit\n3. Change Password");
                System.out.print("Enter your option: ");
                int settingsOption;
                do {
                    try{
                        settingsOption = options.nextInt();
                        if (settingsOption < 1 && settingsOption > 3) {
                            System.out.println("Invalid option! Please try again.\n");
                        }
                        else {
                            settingInterface(settingsOption);
                            break;
                        }
                    }
                    catch (InputMismatchException ime) {
                        System.out.println("Invalid input! Please try again.\n");
                    }
                }
                while (true);
                contPrompt();
                break;
            case 6:
                System.out.println("Thank you for transacting with us.\nHave a nice day!");
                break;
        }
    }


    public void adminOptions(){
        System.out.println("1. View all existing accounts\n2. Create new account for customer\nEnter your option: ");
        Admin a = new Admin();
        Scanner options = new Scanner(System.in);
        int option = options.nextInt();
        switch (option){
            case 1:
                System.out.println("Viewing all accounts");
                ArrayList<Accounts> allAccounts = new ArrayList<Accounts>();
                allAccounts = a.viewAllAccounts();
                for(int i = 0; i < allAccounts.size(); i++){
                    Accounts acc = allAccounts.get(i);
                    String accountNumber = acc.getAccountNumber();
                    String accountName = acc.getAccountName();
                    System.out.println("Account Number: " + accountNumber + ", Account Name: " + accountName);
                }
                break;
            case 2:
                Scanner input = new Scanner(System.in);
                System.out.println("Create new account for customer");
                System.out.print("Enter customer's user id: ");
                String custId = input.nextLine();

                System.out.print("Enter new account number: ");
                String accountNum = input.nextLine();

                System.out.print("Enter new account name: ");
                String accountName = input.nextLine();
                a.createAccount(custId, accountNum, accountName);
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    public void settingInterface(int option){
        Scanner input = new Scanner(System.in);
        Settings setting = new Settings(this.uid);
        setting.readSettings();
        switch (option){
            case 1:
                System.out.println("Current transfer limit: "+ setting.getTransferLimit());
                System.out.println("Set Transfer Limit");
                float limit = input.nextFloat();
                setting.setTransferLimit(limit);
                break;
            case 2:
                System.out.println("Current Overseas limit: "+ setting.getOverseasLimit());
                System.out.println("Overseas Withdraw Limit");
                float overseaLimit = input.nextFloat();
                setting.setOverseasWithdrawLimit(overseaLimit);
                break;
            case 3:
                System.out.println("Change Password");
                System.out.println("Enter New Password");
                String newPassword = input.nextLine();
                setting.changePassword(newPassword);
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
        //input.close();
    }

}
