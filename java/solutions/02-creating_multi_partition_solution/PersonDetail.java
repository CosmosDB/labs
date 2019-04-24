package testpackage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import com.github.javafaker.Faker;
import com.microsoft.azure.cosmosdb.Document;

public class PersonDetail {
    Faker faker = new Faker();
    ArrayList<Document> documentDefinitions = new ArrayList<>();  
     
    public PersonDetail(int number, int childnumber) throws NumberFormatException {
        for (int i= 0; i < number;i++){ 
            ArrayList<Document> children = new ArrayList<>(); 
            for (int j= 0; j < childnumber;j++){  
                Document child= new Document(); 
                child.set("firstName", faker.name().firstName());
                child.set("lastName", faker.name().lastName());
                children.add(child);
            } 
            Document spouse= new Document(); 
            spouse.set("firstName", faker.name().firstName());
            spouse.set("lastName", faker.name().lastName());
            Document relatives = new Document();
            relatives.set("children", children);
            relatives.set("spouse", spouse);
            Document documentDefinition = new Document();    
            Document address = new Document();
            address.set("Street", faker.address().buildingNumber()+" "+faker.address().streetName());                
            address.set("City", faker.address().city());  
            address.set("Country", faker.address().country()); 
            documentDefinition.set("DateOfBirth", faker.date().birthday());         
            documentDefinition.set("firstName", faker.name().firstName());
            documentDefinition.set("lastName", faker.name().lastName());
            documentDefinition.set("PhoneNumber", faker.phoneNumber().phoneNumber());
            documentDefinition.set("Company", faker.company().name());
            documentDefinition.set("Address", address);
            documentDefinition.set("type", "personofinterest");
            documentDefinition.set("type", "personofinterest");
            documentDefinition.set("Relatives", relatives);
            documentDefinitions.add(documentDefinition);
        }    
    }
}