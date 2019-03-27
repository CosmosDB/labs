package testpackage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.github.javafaker.Faker;
import com.microsoft.azure.cosmosdb.Document;

public class PurchaseFoodOrBeverage {
    public DecimalFormat unitPrice;
    public DecimalFormat totalPrice;
    public int quantity;
    public String type;

    Faker faker = new Faker();
    ArrayList<Document> documentDefinitions = new ArrayList<>();  
    public PurchaseFoodOrBeverage(int number) throws NumberFormatException {

        for (int i= 0; i < number;i++){  
            Document documentDefinition = new Document(); 
            DecimalFormat df = new DecimalFormat("###.###");      
            documentDefinition.set("type", "PurchaseFoodOrBeverage");            
            documentDefinition.set("quantity", faker.random().nextInt(1, 5));            
            String unitPrice = df.format(Double.valueOf((Double)faker.random().nextDouble()));
            documentDefinition.set("unitPrice", Double.valueOf(unitPrice));
            int quantity = Integer.valueOf((Integer)documentDefinition.get("quantity"));        
            String totalPrice = df.format(Double.valueOf(unitPrice) * quantity);
            documentDefinition.set("totalPrice", Double.valueOf(totalPrice));
            documentDefinitions.add(documentDefinition);
        }
        
    }
}