package transactions;
import java.util.*;

public abstract class Transfer{
    private int uid;
    private String accountNumber;
    private double localAmount;
    private double overseasAmount;
    private Date date;
    private String details;
    private double withdrawAmount;
    private double depositAmount;
    private double balance;
    private int recipientAccount;
    private Transaction t;

    public Transfer(){

    }

    private Transfer(Date date, String details, double withdrawAmount, double depositAmount, double balance){
        this.date = date;
        this.details = details;
        this.withdrawAmount = withdrawAmount;
        this.depositAmount = depositAmount;
        this.balance = balance;
    }

    public Transfer(int uid, String accountNumber){
        this.uid=uid;
        this.accountNumber=accountNumber;
        this.t=new Transaction(uid,accountNumber);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getUid() {
        return uid;
    }

    public Date getDate() {
        return this.date;
    }

    public double getWithdrawAmount() {
        return this.withdrawAmount;
    }

    public double getDepositAmount() {
        return this.depositAmount;
    }

    public double checkBalance(){
        return this.balance;
    }

    //public boolean transferLocal(double value, String recipientAccNo);

    //public boolean transferOverseas(double value, String recipientAccNo);

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }


    public void inputRecipientAccount(){
        do{
            try{
                Scanner input = new Scanner(System.in);
                System.out.println("Enter account number of recipient: ");
                this.recipientAccount = input.nextInt();
                if (recipientAccount <= 0) {
                    throw new IllegalArgumentException();
                }
                System.out.println("Transfer to " +recipientAccount);
                break;
            }catch (InputMismatchException | IllegalArgumentException e){
                System.out.println("Enter an account number!\n");
            }
        }while (true);
    }

    public int getRecipientAccountNum(){
        return recipientAccount;
    }

    abstract public void transferMoney(int recipientAccount,String currency,Double value);

    abstract public boolean transferSuccess();

    abstract public void transferOptions(int option);

    public void transferInfo(String currency, double transferAmount, double remainingBalance){
        transferMoney(recipientAccount, currency, transferAmount);
        t.transactionWrite(getAccountNumber(),t.getDateCreated(),getDetails(),null,transferAmount,0.00,remainingBalance);
        t.updateBalance(remainingBalance, getAccountNumber());
    }

}
