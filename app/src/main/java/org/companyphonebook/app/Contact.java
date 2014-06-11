package org.companyphonebook.app;

/**
 * Created by blackmaggot on 09.06.14.
 */
public class Contact {
    private String _firstName, _lastName, _email, _phone;
    public Contact(String firstName, String lastName, String phone, String email){
        _firstName = firstName;
        _lastName = lastName;
        _email = email;
        _phone = phone;

    }
    public String getFirstName(){
        return _firstName;
    }
    public String getLastName(){
        return _lastName;
    }
    public String getEmail(){
        return _email;
    }
    public String getPhone(){
        return _phone;
    }
}
