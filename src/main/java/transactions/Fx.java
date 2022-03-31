package transactions;

import java.io.*;

public class Fx {
    private String currency;
    private Double buyRate;
    private Double sellRate;

    public Fx(){}

    public Fx(String currency, Double buyRate, Double sellRate) {
        this.currency = currency;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBuyRate() {
        return buyRate;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void getData(String currency){
        String line = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader("data/overseas_conversion.csv"));
            br.readLine(); //Read the first line so as to not add into hashmap
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(currency)) {
                    this.currency = data[0];
                    this.buyRate = Double.parseDouble(data[1]);
                    this.sellRate = Double.parseDouble(data[2]);
                }
            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}