using Newtonsoft.Json;

public class DeleteStatus
{
    public int Deleted { get; set; }
    public bool Continuation { get; set; }
}

public class Food
{
    [JsonProperty("id")]
    public string Id { get; set; }
    [JsonProperty("description")]
    public string Description { get; set; }
    [JsonProperty("manufacturerName")]
    public string ManufacturerName { get; set; }
    [JsonProperty("foodGroup")]
    public string FoodGroup { get; set; }
}
