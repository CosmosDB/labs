using Bogus;
using System;
using System.Collections.Generic;

public class Family
{
    public Person spouse { get; set; }
    public IEnumerable<Person> children { get; set; }
}

public class Transaction
{
    public string id { get; set; }
    public double amount { get; set; }
    public bool processed { get; set; }
    public string paidBy { get; set; }
    public string costCenter { get; set; }
}

public class Member
{
    public string id { get; set; } = Guid.NewGuid().ToString();
    public Person accountHolder { get; set; }
    public Family relatives { get; set; }
}
