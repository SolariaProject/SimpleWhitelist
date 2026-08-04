package gabrielsynapse.util.fileutil;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gabrielsynapse.util.fileutil.exception.JsonNotFoundException;


public class Json<T>{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type;

    /**
     * @param type:
     *            tipo da classe informar MinhaClasse.class
     */
    public Json(Class<T> type){
        this.type = type;
    }
    /**
     * @param path:
     *            caminho relativo ou absoluto para o arquivo.json
     * @param obj:
     *           objeto para ser serializado do tipo generico
     */
    public void write(String path,T obj){
        String jsonStr = gson.toJson(obj);
        FileUtil.writeFile(path,jsonStr);
    }
    /**
     * @param path:
     *            caminho relativo ou absoluto para o arquivo.json
     * @return tipo generico T
     */
    public T read(String path){
        //verificando se o arquivo nao existe
        if(!FileUtil.isExistFile(path))throw new JsonNotFoundException("O arquivo " + path + " nao existe");
        String jsonStr = FileUtil.readFile(path);
        return gson.fromJson(jsonStr,type);
    }
}