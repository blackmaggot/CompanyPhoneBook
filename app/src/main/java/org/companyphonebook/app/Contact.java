package org.companyphonebook.app;

import android.net.Uri;

/**
 * Created by Wiktor Marchewka on 09.06.14.
 */
public class Contact {
    private String _firstName, _lastName, _email, _phone;
    private  Uri _imageURI;
    public Contact(String firstName, String lastName, String phone, String email, Uri imageURI){
        _firstName = firstName;
        _lastName = lastName;
        _email = email;
        _phone = phone;
        _imageURI = imageURI;

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
    public Uri get_imageURI() {return _imageURI;}
}
