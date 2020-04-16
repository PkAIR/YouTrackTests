package model;

public enum UserGroup {
    AllUsers("All Users"),
    NewUsers("New Users"),
    Reporters("Reporters");

    private String name;

    UserGroup(String groupName) {
        this.name = groupName;
    }

    public String getGroupName() {
        return name;
    }
}
