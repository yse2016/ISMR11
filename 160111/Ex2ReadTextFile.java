import java.io.*;

public class Ex2ReadTextFile {
	public static void main(String[] args) {
		String line;
		try{
		File f = new File("data.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}


		}catch (Exception e) {
		}
	}
}