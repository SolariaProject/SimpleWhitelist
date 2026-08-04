package solaria.simplewhitelist;

public enum Permission {
    ADMIN("simplewhitelist.admin"),
    ADD("simplewhitelist.use.writer"),
    REM("simplewhitelist.use.reader"),
    LIST("simplewhitelist.use.list"),
    RELOAD("simplewhitelist.use.reload");

    public final String KEY;
    Permission(String key){
        this.KEY = key;
    }
}
