package dev.wumie.system.user;

public enum Rank {
    Owner("Owner",2),
    Admin("Admin",1),
    User("User",0);


    public final String name;
    public final int level;

    Rank(String name, int level) {
        this.name = name;
        this.level = level;
    }
}
