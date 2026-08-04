package solaria.simplewhitelist;

import gabrielsynapse.util.fileutil.FileUtil;
import gabrielsynapse.util.fileutil.Json;

import java.util.ArrayList;
import java.util.List;

public class Whitelist {
    private static final Json<Whitelist> JSON = new Json<>(Whitelist.class);
    private static Whitelist whitelist;
    public static final String ROOT = "plugins/SimpleWhitelist";
    public static final String PATH = ROOT + "/whitelist.json";
    public final List<String> players = new ArrayList<>();
    public final transient List<String> rejectedPlayers = new ArrayList<>();
    //metodos setters
    public void add(String nickname){
        this.players.add(nickname);
        this.rejectedPlayers.remove(nickname);
    }
    public void rem(String nickname){
        this.players.remove(nickname);
    }
    //metodos getters
    public boolean hasPlayer(String nickname){
        return this.players.contains(nickname);
    }
    public static void load(){
        Whitelist whitelist = new Whitelist();
        if(FileUtil.isExistFile(PATH)){
            whitelist = JSON.read(PATH);
        }else {
            save(whitelist);
        }
        Whitelist.whitelist = whitelist;
    }
    private static void save(Whitelist whitelist){
        JSON.write(PATH,whitelist);
    }
    public static void save(){
        save(whitelist);
    }
    public static Whitelist getWhitelist(){
        return whitelist;
    }
}
