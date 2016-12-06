// CopyByFileA.java

import java.io.*;

public class CopyByteFileA {
	public static void main(String[] args) {
		try{
		// 1. ストリームをつくる
		FileInputStream in = new FileInputStream("xxxxx.jpg");
		FileInputStream out = new FileInputStream("yyyyy.jpg");

		// 2. データの読み書き
		int d;
		while((d=in.read()) != -1) {
			out.write(d);
		}

		// 3. ストリームを閉じる
		in.close();
		out.close();
		}catch (Exception e) {
			
		}
	}
}