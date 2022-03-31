package user;
import java.io.*;
import java.util.*;

public class Customer extends User{
    private String firstName;
    private String lastName;
    private String address;
    private String dob;


    public Customer() {
    }

    public Customer(String firstName, String lastName, String address, String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dob = dob;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDob() {
        return this.dob;
    }

    //Get all details of the customer based on the user id
    public void getDetails(String userId){
        String line = "";
        int lineNo = 0; //Dont read the first line
        try{
            BufferedReader br = new BufferedReader(new FileReader("data/users.csv"));
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(lineNo > 0){
                    if(userId.equals(data[0])){
                        this.firstName = data[3];
                        this.lastName = data[4];
                        this.address = data[6];
                        this.dob = data[7];
                    }
                }
                lineNo++;
            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
