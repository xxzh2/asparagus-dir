package hf.wj.dir.test;

import hf.wj.dir.Directory;
import net.sf.json.JSONArray;

public class DirectoryTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Directory dir = new Directory("C:\\Users\\leovo\\Desktop\\tmp");
		JSONArray ja =dir.gen(new String[]{"C:\\Users\\leovo\\Desktop\\tmp"});
		System.out.println(ja);
	}

}
