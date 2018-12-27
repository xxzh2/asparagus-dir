package hf.wj.dir.test;

import org.junit.Test;

import hf.wj.dir.Directory;
import net.sf.json.JSONArray;

public class DirectoryTest {

//	@Test
	public void test() {
		Directory dir = new Directory("C:\\Users\\leovo\\Desktop\\tmp");
		JSONArray ja = dir.gen(new String[] { "C:\\Users\\leovo\\Desktop\\tmp" });
		System.out.println(ja);
	}

	@Test
	public void testFormat() {
		System.out.println(String.format("%-3.3s]", "ab"));
	}

}
