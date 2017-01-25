package bigbrother.child_monitoring_system;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by wbroome14 on 1/23/17.
 */
@IgnoreExtraProperties
public class User implements Serializable {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String schoolName;
    private String childFirstName;
    private String childLastName;


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSchoolName(String school) {
        this.schoolName = school;
    }

    public void setChildFirstName(String childFirstName) {
        this.childFirstName = childFirstName;
    }

    public void setChildLastName(String childLastName) {
        this.childLastName = childLastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public String getChildFirstName() {
        return this.childFirstName ;
    }

    public String getChildLastName() {
        return this.childLastName;
    }


}
