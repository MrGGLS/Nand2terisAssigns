import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
        String inFileName = "src/StaticsTest";
        String outFileName = "src/StaticsTest.asm";
//        String inFileName = "src/test.vm";
//        String outFileName = "src/test.asm";
        MyVMTranslator translator = new MyVMTranslator();
        translator.translate(inFileName, outFileName);
    }

    private void translate(String inFileName, String outFileName) throws IOException {
        List<String> lines = preprocess(inFileName);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outFileName));
        boolean hasFunc = checkIfHasFunc(lines);
        if (hasFunc)
            writer.write("@Func.Sys.init\n0;JMP\n"); // Two choices: put init function to the file top or jump to where it is
        for (String line : lines) {
            List<String> fields = parser.parse(line);
            if(fields.get(0).equals("function"))
                hasFunc=true;
            int startIdx = inFileName.lastIndexOf("\\")==-1?0:inFileName.lastIndexOf("\\"); // it should be "/" in linux
            int endIdx = inFileName.lastIndexOf(".")==-1?inFileName.length():(inFileName.lastIndexOf(".")+1);
            String command = codeWriter.toASM(fields, inFileName.substring(startIdx+1,endIdx));
            writer.write(command + "\n");
        }
        writer.close();
    }

    private boolean checkIfHasFunc(List<String> lines) {
        for (String line : lines) {
            if(line.contains("function"))
                return true;
        }
        return false;
    }

    /* Delete all the comments and white spaces
    *  almost the same as in assembler.
    *  But we need to handle directory this time */
    private List<String> preprocess(String inFileName) throws IOException {
        List<String> lines = new ArrayList<>();
        File dirOrFiles = new File(inFileName);
        ArrayList<File> files = new ArrayList<>();
        if(dirOrFiles.isDirectory())
            Collections.addAll(files, dirOrFiles.listFiles());
        else
            files.add(dirOrFiles);
        for (File file : files) {
            String fileName = file.getName();
            if(fileName.contains(".")&&fileName.substring(fileName.lastIndexOf(".")+1).equals("vm")){
                BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()));
                String line;
                String regex = "//.*";
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll(regex, "").trim();
                    if(!line.equals(""))
                        lines.add(line);
                }
                reader.close();
            }
        }
        return lines;
    }
}
