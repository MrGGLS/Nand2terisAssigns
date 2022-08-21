import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MyVMTranslator
 * @Description: A simple vm code translator which only focus on arithmetic, logical and memory access commands
 * @Author: GGLS
 * @Date 2022/7/31
 */
public class MyVMTranslator {
    Parser parser;
    CodeWriter codeWriter;

    MyVMTranslator(){
        parser = new Parser();
        codeWriter = new CodeWriter();
    }

    public static void main(String[] args) throws IOException {
        String inFileName = args[0];
        String outFileName = args[1];
        MyVMTranslator translator = new MyVMTranslator();
        translator.translate(inFileName, outFileName);
    }

    private void translate(String inFileName, String outFileName) throws IOException {
        List<String> lines = preprocess(inFileName);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outFileName));
        for (String line : lines) {
            List<String> fields = parser.parse(line);
            int startIdx = inFileName.lastIndexOf("\\")==-1?0:inFileName.lastIndexOf("\\"); // it should be "/" in linux
            String command = codeWriter.toASM(fields, inFileName.substring(startIdx+1,inFileName.lastIndexOf(".")+1));
            writer.write(command + "\n");
        }
        writer.close();
    }

    /* Delete all the comments and white spaces
    *  almost the same as in assembler */
    private List<String> preprocess(String inFileName) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(inFileName));
        List<String> lines = new ArrayList<>();
        String line;
        String regex = "//.*";
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll(regex, "").trim();
            if(!line.equals(""))
                lines.add(line);
        }
        reader.close();
        return lines;
    }
}
