package testpackage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import com.github.javafaker.Faker;
import com.microsoft.azure.cosmosdb.Document;

public class Person {
    Faker faker = new Faker();
    ArrayList<Object> documentDefinitions = new ArrayList<>();  
    public Person(int number) throws NumberFormatException {
        for (int i= 0; i < number;i++){  
            Document documentDefinition = new Document();          
            documentDefinition.set("firstName", faker.name().firstName());
            documentDefinition.set("lastName", faker.name().lastName());
            documentDefinition.set("company", "contosofinancial");
            String docdef = documentDefinition.toString();
            documentDefinitions.add(docdef);
        }    
    }
}