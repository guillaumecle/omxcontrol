import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
        Process omx = Runtime.getRuntime().exec("omxplayer ~/music/Our\\ Story.mp3");
        new StdReader(omx).start();
        new ErrReader(omx).start();
    }

    public static class StdReader extends Thread {

        private Process omx;

        public StdReader(Process omx) {
            this.omx = omx;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(omx.getInputStream()));
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("omx instance is terminated");
        }
    }

    public static class ErrReader extends Thread {

        private Process omx;

        public ErrReader(Process omx) {
            this.omx = omx;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(omx.getErrorStream()));
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    System.err.println("#" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("omx instance is terminated");
        }
    }
}
