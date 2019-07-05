
public interface IInteraction
{
    string type { get; }
}

public class GeneralInteraction : IInteraction
{
    public string id { get; set; }

    public string type { get; set; }
}

public class PurchaseFoodOrBeverage : IInteraction
{
    public string id { get; set; }
    public decimal unitPrice { get; set; }
    public decimal totalPrice { get; set; }
    public int quantity { get; set; }
    public string type { get; set; }
}

public class ViewMap : IInteraction
{
    public string id { get; set; }
    public int minutesViewed { get; set; }
    public string type { get; set; }
}

public class WatchLiveTelevisionChannel : IInteraction
{
    public string id { get; set; }
    public string channelName { get; set; }
    public int minutesViewed { get; set; }
    public string type { get; set; }
}
