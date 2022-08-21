import javax.rmi.CORBA.Util;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: Code
 * @Description: Map fields to binary code
 * @Author: GGLS
 * @Date 2022/7/30
 */
public class Code {
    HashMap<String, String> c2b; //command to binary

    Code() {
        c2b = new HashMap<>();
        // when a = 0
        c2b.put("0", "101010");
        c2b.put("1", "111111");
        c2b.put("-1", "111010");
        c2b.put("D", "001100");
        c2b.put("A", "110000");
        c2b.put("!D", "001101");
        c2b.put("!A", "110001");
        c2b.put("-D", "001111");
        c2b.put("-A", "110011");
        c2b.put("D+1", "011111");
        c2b.put("A+1", "110111");
        c2b.put("D-1", "001110");
        c2b.put("A-1", "110010");
        c2b.put("D+A", "000010");
        c2b.put("D-A", "010011");
        c2b.put("A-D", "000111");
        c2b.put("D&A", "000000");
        c2b.put("D|A", "010101");
        // when a = 1
        c2b.put("M", "110000");
        c2b.put("!M", "110001");
        c2b.put("-M", "110011");
        c2b.put("M+1", "110111");
        c2b.put("M-1", "110010");
        c2b.put("D+M", "000010");
        c2b.put("D-M", "010011");
        c2b.put("M-D", "000111");
        c2b.put("D&M", "000000");
        c2b.put("D|M", "010101");
        // dest
        c2b.put("null", "000");
        c2b.put("dM", "001");
        c2b.put("dD", "010");
        c2b.put("dMD", "011");
        c2b.put("dA", "100");
        c2b.put("dAM", "101");
        c2b.put("dAD", "110");
        c2b.put("dAMD", "111");
        // jmp
        c2b.put("JGT", "001");
        c2b.put("JEQ", "010");
        c2b.put("JGE", "011");
        c2b.put("JLT", "100");
        c2b.put("JNE", "101");
        c2b.put("JLE", "110");
        c2b.put("JMP", "111");
    }

    public String toBinary(List<String> fields, SymbolTable table) {
        String binaryCode = "";
        if (fields.size() == 1) { // A instruction
            if (isUnsignedInteger(fields.get(0))) {
                binaryCode += Utils.int16ToBinary(Integer.parseInt(fields.get(0)));
            }else{
                if(!table.contains(fields.get(0))){
                    table.set(fields.get(0), table.getPointer());
                }
                binaryCode += Utils.int16ToBinary(table.get(fields.get(0)));
            }
        } else { // C instruction
            binaryCode += "111";
            if (fields.get(0).equals("=")) { // dest = comp
                if (!fields.get(2).contains("M")) {
                    binaryCode += "0" + c2b.get(fields.get(2));
                } else {
                    binaryCode += "1" + c2b.get(fields.get(2));
                }
                binaryCode += c2b.get("d"+fields.get(1)) + c2b.get("null");
            } else { // comp ; jmp
                if (!fields.get(2).contains("M")||fields.get(2).equals("JMP")) {
                    binaryCode += "0" + c2b.get(fields.get(1));
                } else {
                    binaryCode += "1" + c2b.get(fields.get(1));
                }
                binaryCode += c2b.get("null") + c2b.get(fields.get(2));
            }
        }
        return binaryCode;
    }

    // We dont consider "-/+" here
    private boolean isUnsignedInteger(String str) {
        if (str.length() < 1)
            return false;
        for (char c : str.toCharArray()) {
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

}
