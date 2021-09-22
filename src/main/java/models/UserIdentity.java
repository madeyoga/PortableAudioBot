package models;

public class UserIdentity {
    private final String id;
    private final String name;

    public UserIdentity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
