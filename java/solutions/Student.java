package testpackage;

import com.google.gson.Gson;
import com.microsoft.azure.cosmosdb.Document;

public class Student
{
    private static Gson gson = new Gson();
    public Student(Document doc){
        
        Document Clubs = new Document(gson.toJson(doc));
    }

}