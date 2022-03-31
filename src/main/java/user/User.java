package user;
import java.util.*;
import java.io.*;

public class User {
    private int userId;
    private String username;
    private String password;


    public User() {
    }

    private User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId(){
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }

    private String getPassword(){
        return this.password;
    }

    private void changePassword(String password) {
        this.password = password;
    }

    public Boolean login(String username, String password){
        if(validateAccount(username, password)){
            return true;
        }
        else{
            return false;
        }

    }

    private Boolean validateAccount(String username, String password){
        Boolean result = false;
        ArrayList<User> users = getListOfUsers();
        for (int i=0; i < users.size(); i++){
            User user = users.get(i);
            //Check if user exists in records
            if(username.equals(user.getUsername())){
                //This is such user in the records, validate the password
                if(password.equals(user.getPassword())){
                    this.userId = user.getUserId(); //Set the userId for the current logged in user
                    result = true;
                    break;
                }
                else{
                    //User is in the records but the password is incorrect
                    result = false;
                }
            }
            //User is not in the records
            else{
                result = false;
            }
        }
        if(result == false){
            System.out.println("Your username or password is invalid.");
        }
        return result;

    }

    //Get all existing users from users.csv and store it in a hashmap
    private ArrayList<User> getListOfUsers(){
        ArrayList<User> users = new ArrayList<User>();
        String line = "";
        int lineNo = 0; //Dont read the first line
        try{
            BufferedReader br = new BufferedReader(new FileReader("data/users.csv"));
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(lineNo > 0){
                    users.add(new User(Integer.parseInt(data[0]), data[1], data[2]));
                }
                lineNo++;
            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        return users;
    }

    public String getType(String userId){
        String type = "";
        String line = "";
        int lineNo = 0; //Dont read the first line
        try{
            BufferedReader br = new BufferedReader(new FileReader("data/users.csv"));
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(lineNo > 0){
                    if(userId.equals(data[0])){
                        type = data[5];
                        break;
                    }
                }
                lineNo++;
            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return type;
    }
}
