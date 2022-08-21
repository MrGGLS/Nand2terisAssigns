import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Parser
 * @Description: Translate command to several fields
 * @Author: GGLS
 * @Date 2022/7/30
 */
public class Parser {
    List<String> parse(String line, SymbolTable table) {
        List<String> fields = new ArrayList<>();
        if (line.charAt(0) == '@') { // A instruction
            fields.add(line.substring(1));
        } else { // C instruction
            if (line.contains("=")) {
                String[] split = line.split("=");
                fields.add("=");
                fields.add(split[0]);
                fields.add(split[1]);
            } else if (line.contains(";")) {
                String[] split = line.split(";");
                fields.add(";");
                fields.add(split[0]);
                fields.add(split[1]);
            }
        }
        return fields;
    }
}
