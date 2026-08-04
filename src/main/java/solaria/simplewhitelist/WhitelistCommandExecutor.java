package solaria.simplewhitelist;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistCommandExecutor implements CommandExecutor, TabCompleter {
    private boolean checkPermission(CommandSender commandSender,Permission... permissions){
        if(commandSender instanceof Player player){
            UUID uuid = player.getUniqueId();
            User user = Global.getLuckPerms().getUserManager().getUser(uuid);
            if(user != null){
                for(Permission permission:permissions) {
                    if(user.getCachedData().getPermissionData().checkPermission(permission.KEY).asBoolean()){
                        return true;
                    }
                }
            }
        }else{
            return commandSender.isOp();
        }
        return false;
    }
    private void permissionFound(CommandSender commandSender){
        commandSender.sendMessage("voce nao tem permissao para usar esse comando");
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(checkPermission(commandSender,Permission.ADMIN,Permission.ADD,Permission.REM,Permission.RELOAD)){
            if(args[0].equals("reload")){
                if(!checkPermission(commandSender,Permission.ADMIN,Permission.RELOAD)){
                    this.permissionFound(commandSender);
                    return true;
                }
                commandSender.sendMessage("recarrecando whitelist");
                Whitelist.load();
            }else if(args[0].equals("add")){
                if(!checkPermission(commandSender,Permission.ADMIN,Permission.ADD)){
                    this.permissionFound(commandSender);
                    return true;
                }
                String nickname = args[1];
                if(Whitelist.getWhitelist().hasPlayer(nickname)){
                    commandSender.sendMessage("O jogador " + nickname + " já esta na whitelist");
                    return true;
                }
                commandSender.sendMessage("jogador " + nickname + " adicionado a whitelist");
                Whitelist.getWhitelist().add(nickname);
                Whitelist.save();
            }else if(args[0].equals("rem")){
                if(!checkPermission(commandSender,Permission.ADMIN,Permission.REM)){
                    this.permissionFound(commandSender);
                    return true;
                }
                String nickname = args[1];
                //verificando se o jogador esta na whitelist
                if(!Whitelist.getWhitelist().hasPlayer(nickname)){
                    commandSender.sendMessage("O jogador só pode ser removido da whitelist se estiver nela");
                    return true;
                }
                commandSender.sendMessage("jogador " + nickname + " foi removido da whitelist");

                Whitelist.getWhitelist().rem(nickname);
                Whitelist.save();
            }else if(args[0].equals("list")){
                if(!checkPermission(commandSender,Permission.ADMIN,Permission.LIST)){
                    this.permissionFound(commandSender);
                    return true;
                }
                List<String> nicks = Whitelist.getWhitelist().players;
                if(nicks.isEmpty()){
                    commandSender.sendMessage("A Lista de jogadores esta vazia");
                }else {
                    commandSender.sendMessage("Lista de jogadores");
                    for(String nick:nicks){
                        commandSender.sendMessage(">>>>>>>> " + nick);
                    }
                }
            }else {
                return false;
            }
            return true;
        }else {
            permissionFound(commandSender);
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        List<String> sujestions = new ArrayList<>();
        if(args.length == 1){
            sujestions.add("add");
            sujestions.add("rem");
            sujestions.add("list");
            sujestions.add("reload");
        }else if(args.length == 2){
            if(args[0].equals("add")){
                sujestions.addAll(Whitelist.getWhitelist().rejectedPlayers);
            }else if(args[0].equals("rem")){
                sujestions.addAll(Whitelist.getWhitelist().players);
            }
        }
        return sujestions;
    }
}
