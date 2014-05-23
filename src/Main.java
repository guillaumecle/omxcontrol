import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
        Process omx = Runtime.getRuntime().exec("omxplayer ~/music/*");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(omx.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("omx instance is terminated");
    }

}
