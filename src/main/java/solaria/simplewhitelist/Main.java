package solaria.simplewhitelist;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new WorldEvents(),this);
        PluginCommand commandSW = this.getCommand("swl");
        if(commandSW != null) {
            WhitelistCommandExecutor whitelistCommandExecutor = new WhitelistCommandExecutor();
            commandSW.setExecutor(whitelistCommandExecutor);
            commandSW.setTabCompleter(whitelistCommandExecutor);
        }

        Global.inicialize(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
