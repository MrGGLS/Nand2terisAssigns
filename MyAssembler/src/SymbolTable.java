import java.util.HashMap;

/**
 * @ClassName: SymbolTable
 * @Description: A table for storing symbols and their memory addresses
 * @Author: GGLS
 * @Date 2022/7/30
 */
public class SymbolTable {
    private final HashMap<String, Integer> map;
    private int pointer;
    SymbolTable() {
        map = new HashMap<String, Integer>();
        for (int i = 0; i < 16; i++)
            map.put("R" + i, i);
        pointer=16;
        map.put("SCREEN", 16384);
        map.put("KBD", 24576);
        map.put("SP", 0);
        map.put("LCL", 1);
        map.put("ARG", 2);
        map.put("THIS", 3);
        map.put("THAT", 4);
    }

    public void set(String key, int value) {
        map.put(key, value);
    }

    public int get(String key) {
        return map.get(key);
    }

    public boolean contains(String key){
        return map.containsKey(key);
    }

    public int getPointer(){
        return pointer++;
    }
}
