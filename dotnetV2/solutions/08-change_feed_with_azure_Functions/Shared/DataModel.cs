using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Shared
{
    public enum ActionType
    {
        Viewed,
        Added,
        Purchased
    }

    public class CartAction
    {
        [JsonProperty("id")]
        public string Id { get; set; }

        public int CartId { get; set; }
        [JsonConverter(typeof(StringEnumConverter))]
        public ActionType Action { get; set; }
        public string Item { get; set; }
        public double Price { get; set; }
        public string BuyerState { get; set; }

        public CartAction()
        {
            Id = Guid.NewGuid().ToString();
        }
    }

    public class StateCount
    {
        [JsonProperty("id")]
        public string Id { get; set; }
        public string State { get; set; }
        public int Count { get; set; }
        public double TotalSales { get; set; }

        public StateCount()
        {
            Id = Guid.NewGuid().ToString();
        }
    }
}
