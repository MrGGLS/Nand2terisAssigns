/**
 * @ClassName: Utils
 * @Description: utils for building assembler
 * @Author: GGLS
 * @Date 2022/7/30
 */
public class Utils {
    public static String int16ToBinary(int n){
        char[] chs = new char[16];
        for(int i=15;i>=0;i--){
            int base = 1<<i;
            if(n>=base){
                chs[15-i]='1';
                n-=base;
            }else
                chs[15-i]='0';
        }
        return new String(chs);
    }
}
