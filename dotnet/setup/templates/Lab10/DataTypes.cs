using System.Collections.Generic;

public class Tag
{
    public string name { get; set; }
}

public class Food
{
    public string id { get; set; }
    public string description { get; set; }
    public List<Tag> tags { get; set; }
    public string foodGroup { get; set; }
}
