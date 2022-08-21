import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MyAssembler
 * @Description: A simple assembler
 * @Author: GGLS
 * @Date 2022/7/30
 */
public class MyAssembler {
    private Parser parser;
    private Code code;
    private SymbolTable table;

    MyAssembler(){
        parser=new Parser();
        code = new Code();
        table = new SymbolTable();
    }

    public static void main(String[] args) throws IOException {
        String inFileName = args[0];
        String outFileName = args[1];
        MyAssembler assembler = new MyAssembler();
        assembler.translate(inFileName, outFileName);
    }

    public void translate(String inFileName, String outFileName) throws IOException {
        List<String> lines = preprocess(inFileName, table);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outFileName));
        for (String line : lines) {
            List<String> fields = parser.parse(line, table);
            String binary = code.toBinary(fields, table);
            writer.write(binary + "\r\n");
        }
        writer.close();
    }

    /* Delete all the comments and map label to the related code line */
    private List<String> preprocess(String inFileName, SymbolTable table) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(inFileName));
        List<String> lines = new ArrayList<>();
        String line;
        String regex = "//.*";
        int lineNo = 0;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll(regex, "").trim();
            if (!line.equals("")) {
                if (line.contains("(")) {
                    line = line.substring(1, line.length() - 1);
                    table.set(line, lineNo);
                } else {
                    lines.add(line);
                    lineNo++;
                }
            }
        }
        reader.close();
        return lines;
    }
}
