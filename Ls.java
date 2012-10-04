import java.io.File;
import java.io.IOException;

// See http://jonisalonen.com/2012/java-and-file-names-with-invalid-characters/
class Ls {
    public static void main(String[] args) throws IOException {
        File d = new File("src/main/java/com/aslakhellesoy");
        for (File f : d.listFiles()) {
            System.out.printf("%s: %b\n", f.getName(), f.exists());
        }
    }
}