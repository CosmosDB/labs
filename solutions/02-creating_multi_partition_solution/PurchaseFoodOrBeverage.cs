public class PurchaseFoodOrBeverage : IInteraction
{
    public decimal unitPrice { get; set; }
    public decimal totalPrice { get; set; }
    public int quantity { get; set; }
    public string type { get; set; }
}