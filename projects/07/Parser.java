import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: Parser
 * @Description: a simple parser
 * @Author: GGLS
 * @Date 2022/7/31
 */
public class Parser {
    List<String> parse(String line) {
        String[] fields = line.split(" ");
        List<String> newFields = new ArrayList<>();
        Collections.addAll(newFields, fields);
        return newFields;
    }
}
