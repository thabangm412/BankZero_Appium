package models;

public class User {

    private final String profileName;
    private final String loginPin;

    public User(String profileName, String loginPin) {
        this.profileName = profileName;
        this.loginPin = loginPin;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getLoginPin() {
        return loginPin;
    }
}