import java.io.*;
//import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.text.DecimalFormat;

import javax.naming.InsufficientResourcesException;

import transactions.*;
import user.*;


class Main{
    public static void main(String[] arg){
        String username, password;
        Boolean success;
        User user = new User();
        Customer customer = new Customer();
        Admin admin = new Admin();
        Scanner input = new Scanner(System.in);

        // Loop till valid login credentials are provided
        do {
            System.out.print("Enter username: ");
            username = input.nextLine();
            System.out.print("Enter password: ");
            password = input.nextLine();

            success = user.login(username, password);
            if(success == false) {
                System.out.println("Please try again!\n");
            }
        }
        while(success != true);
        //input.close();

        // Program runs if login is successfull
        if(success){
            int userId = user.getUserId();
            String type = user.getType(userId + "");
            //System.out.println("You have successfully logged in.");
            System.out.println("Your uid is " + userId);

            //UI object
            UserPrompt ui = new UserPrompt(userId);

            if(type.equals("customer")){
                customer.getDetails(userId + "");

                //System.out.println("Accounts under you: " + userAcct.getAllAccounts(user.getUserId() + ""));
                System.out.println(customer.getAddress() + " " + customer.getDob());

                ui.option(userId + "");
            }
            else if(type.equals("admin")){
                ui.adminOptions();
            }

        }
    }

}
