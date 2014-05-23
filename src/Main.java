import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		Omx omx = new Omx();
		omx.startPlaying("/home/pi/music/Our Story.mp3");
        Reader bf = new InputStreamReader(System.in);
        while (bf.read() != -1) {
           omx.pause();
        }
    }

}
