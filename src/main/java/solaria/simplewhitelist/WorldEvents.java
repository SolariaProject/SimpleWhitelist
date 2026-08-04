package solaria.simplewhitelist;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class WorldEvents implements Listener {
    @EventHandler
    public void OnAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event){
        String name = event.getName();
        if(!Whitelist.getWhitelist().hasPlayer(name)){
            if(!Whitelist.getWhitelist().rejectedPlayers.contains(name)) {
                Whitelist.getWhitelist().rejectedPlayers.add(name);
            }
            event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST,
                Component.text("Você não esta na whitelist")
            );
        }
    }
}
