public class StudentProfile
{
    public string Id { get; set; }
    public string Name { get; set; }
    public StudentProfileEmailInformation Email { get; set; }
}

public class StudentProfileEmailInformation
{
    public string Home { get; set; }
    public string School { get; set; }
}