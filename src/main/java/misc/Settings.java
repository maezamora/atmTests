package misc;

import java.util.*;

import user.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Settings{
    private int uid;
    private float transferLimit;
    private float overseasWithdrawLimit;
    HashMap<Integer,Float> transferlimitMap = new HashMap<Integer,Float>();
    HashMap<Integer,Float> overseaslimitMap = new HashMap<Integer,Float>();
    User user = new User();

    public Settings(){
    }

    public Settings(int uid) {
        this.uid = uid;
    }

    public void setTransferLimit(float limit){
        File oldFile = new File("data/settings.csv");

        this.transferLimit = limit;
        //edit transferlimit on settings.csv
        fileEditor(oldFile, 1, this.transferLimit,"",false);
    }

    public void setOverseasWithdrawLimit(Float limit){
        File oldFile = new File("data/settings.csv");
        this.overseasWithdrawLimit = limit;

        //edit overseawithdrawLimit on settings.csv
        fileEditor(oldFile, 2, this.overseasWithdrawLimit,"",false);
    }

    public void changePassword(String newPass){
        File oldFile = new File("data/users.csv");
        //edit password at user.csv
        fileEditor(oldFile, 2, 0,newPass,true);
    }

    public float getTransferLimit(){
        return this.transferLimit;

    }

    public float getOverseasLimit(){

        return this.overseasWithdrawLimit;

    }

    public void fileEditor(File oldFile, int element, float changedValue,String newPassword,Boolean changePassword){
        String newUid = String.valueOf(this.uid);
        List<String> newLines = new ArrayList<>();
        String[] column;
        try{
            //Iterate line by line of file that the program want to edit
            for (String line : Files.readAllLines(Paths.get(oldFile.toURI()), StandardCharsets.UTF_8)) {
                column = line.split(",");
                //if uid at row equals to current user's uid
                if(column[0].equals(newUid)){
                    if(!changePassword){
                        //System.out.println(column[1] + " " + column[2]);
                        //If updating transfer limit column, do..
                        if(element == 1){
                            //This line replaces only the transfer limit and keeps the overseas limit with
                            //the previous value if both overseas and transfer limit value were same previously.
                            newLines.add(line.replace(column[element]+","+column[2], changedValue + ","+column[2]));
                        }
                        else if(element == 2){
                            //This line replaces only the overseas limit and keeps the original transfer limit with the
                            //previous value if both overseas and transfer limit value were the same previously.
                            newLines.add(line.replace(column[1]+","+column[element], column[1] + "," + changedValue));
                        }
                    }else{
                        newLines.add(line.replace(column[2], newPassword + ""));
                    }


                }else{
                    newLines.add(line);
                }
            }
            Files.write(Paths.get(oldFile.toURI()), newLines, StandardCharsets.UTF_8);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //Get all existing users from users.csv and store it in a hashmap
    public void readSettings(){
        String line = "";
        int lineNo = 0; //Dont read the first line
        try{
            BufferedReader br = new BufferedReader(new FileReader("data/settings.csv"));
            //read settings.csv file line by line and out into hash map
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(lineNo > 0){
                    transferlimitMap.put(Integer.parseInt(data[0]), Float.parseFloat(data[1]));
                    overseaslimitMap.put(Integer.parseInt(data[0]), Float.parseFloat(data[2]));
                }

                lineNo++;
            }
            br.close();


            for(int i =0; i< overseaslimitMap.size(); i++){
                this.overseasWithdrawLimit = overseaslimitMap.get(this.uid);
            }

            for(int i =0; i< transferlimitMap.size(); i++){
                this.transferLimit = transferlimitMap.get(this.uid);
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
