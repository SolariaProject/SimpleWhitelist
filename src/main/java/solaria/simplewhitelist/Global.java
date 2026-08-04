package solaria.simplewhitelist;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.Plugin;

public class Global {
    private static Plugin plugin;

    private static LuckPerms luckPerms;

    protected static void inicialize(Plugin plugin){
        Global.plugin = plugin;
        Whitelist.load();
        luckPerms = LuckPermsProvider.get();
    }

    //metodos setters

    //metodos getters
    public static Plugin getPlugin(){
        return plugin;
    }

    public static LuckPerms getLuckPerms(){
        return luckPerms;
    }
}
