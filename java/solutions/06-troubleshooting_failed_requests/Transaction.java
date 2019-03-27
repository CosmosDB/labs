package testpackage;
import java.util.ArrayList;
import com.github.javafaker.Faker;
import com.microsoft.azure.cosmosdb.Document;

public class Transaction {
    Faker faker = new Faker();
    ArrayList<Document> documentDefinitions = new ArrayList<>();  
    public Transaction(int number) throws NumberFormatException {
        for (int i= 0; i < number;i++){  
            Document documentDefinition = new Document();      
            documentDefinition.set("amount", faker.random().nextDouble());            
            documentDefinition.set("processed", faker.random().nextBoolean());            
            documentDefinition.set("paidBy", faker.name().firstName() +" "+faker.name().lastName());
            documentDefinition.set("costCenter", faker.commerce().department());
            documentDefinition.set("type", "test");
            documentDefinitions.add(documentDefinition);
        }      
    }
}