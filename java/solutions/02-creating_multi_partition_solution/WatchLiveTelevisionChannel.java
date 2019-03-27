package testpackage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import com.github.javafaker.Faker;
import com.microsoft.azure.cosmosdb.Document;

public class WatchLiveTelevisionChannel {
    public DecimalFormat unitPrice;
    public DecimalFormat totalPrice;
    public int quantity;
    public String type;

    Faker faker = new Faker();
    ArrayList<Document> documentDefinitions = new ArrayList<>();  
    public WatchLiveTelevisionChannel(int number) throws NumberFormatException {

        for (int i= 0; i < number;i++){  
            Document documentDefinition = new Document(); 
            DecimalFormat df = new DecimalFormat("###.###");      
            documentDefinition.set("type", "WatchLiveTelevisionChannel");   
            String[] arr={"NEWS-6", "DRAMA-15", "ACTION-12", "DOCUMENTARY-4", "SPORTS-8"};
            Random r=new Random();
            int randomNumber=r.nextInt(arr.length);        
            documentDefinition.set("channelName", arr[randomNumber]);            
            documentDefinition.set("minutesViewed", faker.random().nextInt(1, 45));
            documentDefinitions.add(documentDefinition);
        }
        
    }
}