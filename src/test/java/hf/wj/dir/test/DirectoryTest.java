package hf.wj.dir.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import hf.wj.dir.Directory;
import net.sf.json.JSONArray;

public class DirectoryTest {

//	@Test
	public void test() throws UnsupportedEncodingException {
		Directory dir = new Directory("C:\\Users");
		JSONArray ja = dir.gen(new String[] { "C:\\Users" });
		System.out.println(ja);
	}

	@Test
	public void encodePath() {
		String dir = Directory.encodePath("\\Bing-wallpaper\\Number2_ZH-CN12009255937_1920x1080.jpg");
		System.out.println(dir);
	}

	@Test
	public void testFormat() {
		System.out.println(String.format("%-3.3s]", "ab"));
	}

}
