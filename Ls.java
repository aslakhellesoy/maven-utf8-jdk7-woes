import java.io.File;
import java.io.IOException;

// See http://jonisalonen.com/2012/java-and-file-names-with-invalid-characters/
class Ls {
    public static void main(String[] args) throws Exception {
        File d = new File("src/main/java/com/aslakhellesoy");
        for (File f : d.listFiles()) {
            if(args.length > 0 && "hex".equals(args[0])) {
                System.out.println(toHex(f.getName()));
            } else {
                System.out.printf("%s: %b\n", f.getName(), f.exists());
            }
        }
    }

    private static String toHex(String arg) throws Exception {
        return String.format("%040x", new java.math.BigInteger(arg.getBytes("UTF-8")));
    }
}