import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: CodeWriter
 * @Description: deal with parser output
 * @Author: GGLS
 * @Date 2022/7/31
 */
public class CodeWriter {
    private final HashMap<String, String> c2a;
    private int jmpID;
    private int counter;

    CodeWriter() {
        c2a = new HashMap<>();
        c2a.put("local", "LCL");
        c2a.put("argument", "ARG");
        c2a.put("this", "THIS");
        c2a.put("that", "THAT");
        c2a.put("temp", "5");
        c2a.put("0", "THIS");
        c2a.put("1", "THAT");
        jmpID = 0;
        counter = 0;
    }

    public int getJmpID() {
        return jmpID++;
    }

    public String toASM(List<String> fields, String inFileName) {
        String command = "// ";
        for (int i = 0; i < fields.size(); i++)
            command += fields.get(i) + " ";
        command += "\n";  // just tell what the following command will do;
        if (fields.size() == 1) { // logical command
            String id = Integer.toString(getJmpID());
            switch (fields.get(0)) {
                case "add":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "M=D+M";
                    break;
                case "sub":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "M=M-D";
                    break;
                case "neg":
                    command += "@SP\n" +
                            "A=M-1\n" +
                            "M=-M";
                    break;
                case "eq":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "@GGLS_JMP_" + id + "\n" +
                            "D;JNE\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "M=-1\n" +
                            "@GGLS_JMP_END_" + id + "\n" +
                            "0;JMP\n" +
                            "(GGLS_JMP_" + id + ")\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "M=0\n" +
                            "(GGLS_JMP_END_" + id + ")";
                    break;
                case "gt":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "@GGLS_JMP_" + id + "\n" +
                            "D;JLE\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "M=-1\n" +
                            "@GGLS_JMP_END_" + id + "\n" +
                            "0;JMP\n" +
                            "(GGLS_JMP_" + id + ")\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "M=0\n" +
                            "(GGLS_JMP_END_" + id + ")";
                    break;
                case "lt":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "@GGLS_JMP_" + id + "\n" +
                            "D;JGE\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "M=-1\n" +
                            "@GGLS_JMP_END_" + id + "\n" +
                            "0;JMP\n" +
                            "(GGLS_JMP_" + id + ")\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "D=M-D\n" +
                            "M=0\n" +
                            "(GGLS_JMP_END_" + id + ")";
                    break;
                case "and":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "M=D&M";
                    break;
                case "or":
                    command += "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "A=M-1\n" +
                            "M=D|M";
                    break;
                case "not":
                    command += "@SP\n" +
                            "A=M-1\n" +
                            "M=!M";
                    break;
                case "return":
                    command += "@LCL\nD=M\n@endFrame\nM=D\n"; // create temp variable
                    command += "@endFrame\nD=M\n@5\nA=D-A\nD=M\n@retAddr\nM=D\n"; // get returnAddr
                    command += "@SP\nM=M-1\nA=M\nD=M\n@ARG\nA=M\nM=D\n"; // *ARG=pop()
                    command += "@ARG\nD=M\n@1\nD=D+A\n@SP\nM=D\n"; // SP=ARG+1
                    command += "@endFrame\nD=M\n@1\nA=D-A\nD=M\n@THAT\nM=D\n"; // THAT=*(endFrame-1)
                    command += "@endFrame\nD=M\n@2\nA=D-A\nD=M\n@THIS\nM=D\n"; //
                    command += "@endFrame\nD=M\n@3\nA=D-A\nD=M\n@ARG\nM=D\n";
                    command += "@endFrame\nD=M\n@4\nA=D-A\nD=M\n@LCL\nM=D\n";
                    command += "@retAddr\nA=M\n0;JMP"; // goto retAddr
                    break;
            }
        } else if (fields.size()==2){ // handle goto
            if(fields.get(0).equals("label")){ // label xxx
                command += "(GGLS_"+fields.get(1)+")";
            }else if(fields.get(0).equals("goto")){ // goto xxx
                command += "@GGLS_"+fields.get(1)+"\n0;JMP";
            }else{  // if-goto xxx
                command += "@SP\n" +
                        "M=M-1\n" +
                        "A=M\n" +
                        "D=M\n@GGLS_" + fields.get(1) +"\n" +
                        "D;JNE";
            }
        } else if (fields.size() == 3) { // push / pop segment i
            if (fields.get(0).equals("push")) {
                switch (fields.get(1)) { // which segment ?
                    case "local":
                    case "argument":
                    case "this":
                    case "that":
                        command += "@" + c2a.get(fields.get(1)) + "\n" +
                                "D=M\n" +
                                "@" + fields.get(2) + "\n" +
                                "A=D+A\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1";
                        break;
                    case "constant":
                        command += "@"+fields.get(2)+"\n" +
                                "D=A\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1";
                        break;
                    case "static":
                        command += "@"+inFileName.replaceAll("/",".")+fields.get(2)+"\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1";
                        break;
                    case "temp":
                        command += "@" + c2a.get(fields.get(1)) + "\n" +
                                "D=A\n" +
                                "@" + fields.get(2) + "\n" +
                                "A=D+A\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1";
                        break;
                    case "pointer":
                        command += "@"+c2a.get(fields.get(2))+"\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1";
                        break;
                }
            } else if (fields.get(0).equals("pop")) {
                switch (fields.get(1)) { // which segment ?
                    case "local":
                    case "argument":
                    case "this":
                    case "that":
                        command += "@SP\n" +
                                "M=M-1\n" +
                                "@" + c2a.get(fields.get(1)) + "\n" +
                                "D=M\n" +
                                "@" + fields.get(2) + "\n" +
                                "A=D+A\n" +
                                "D=A\n" +
                                "@R13\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@R13\n" +
                                "A=M\n" +
                                "M=D";
                        break;
                    case "static":
                        command += "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@"+inFileName.replaceAll("/",".")+fields.get(2)+"\n" +
                                "M=D";
                        break;
                    case "temp":
                        command += "@SP\n" +
                                "M=M-1\n" +
                                "@" + c2a.get(fields.get(1)) + "\n" +
                                "D=A\n" +
                                "@" + fields.get(2) + "\n" +
                                "A=D+A\n" +
                                "D=A\n" +
                                "@R13\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@R13\n" +
                                "A=M\n" +
                                "M=D";
                        break;
                    case "pointer":
                        command += "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@"+c2a.get(fields.get(2))+"\n" +
                                "M=D";
                        break;
                }
            }else if(fields.get(0).equals("call")){
                // save returnAddr and states
                command += "@Return.Func."+fields.get(1)+Integer.toString(counter)+"\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +
                        "@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +
                        "@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +
                        "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +
                        "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                /* ARG = SP-5-nArgs
                * LCL = SP
                * goto functionName*/
                command += "@SP\nD=M\n@5\nD=D-A\n@"+fields.get(2)+"\nD=D-A\n@ARG\nM=D\n" +
                        "@SP\nD=M\n@LCL\nM=D\n" +
                        "@Func."+fields.get(1)+"\n0;JMP\n"+
                        "(Return.Func."+fields.get(1)+Integer.toString(counter)+")";
                counter++;
                /* We need not do anything for handling the function content
                * Just treat it as normal command */
            }else{ // function xxx x
                command += "(Func."+fields.get(1)+")";
            }
        }
        return command;
    }
}
