package android.com.freezeframe;

public class User {
    private String username, phonenumber, secquestion, secanswer, email, fullName;

    public User(String username, String phonenumber, String secquestion, String secanswer, String email, String fullName)
    {
        this.username = username;
        this.phonenumber = phonenumber;
        this.secquestion = secquestion;
        this.secanswer = secanswer;
        this.email = email;
        this.fullName = fullName;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPhoneNumber()
    {
        return phonenumber;
    }

    public String getSecquestion()
    {
        return secquestion;
    }

    public String getSecanswer()
    {
        return secanswer;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setSecquestion(String question)
    {
        secquestion = question;
    }

    public void setSecanswer(String answer)
    {
        secanswer = answer;
    }
}
