package hf.wj.dir.test;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;

import hf.wj.dir.Directory;
import net.sf.json.JSONArray;

public class DirectoryTest {

	final Log log = LogFactory.getLog(this.getClass());

	@Test
	@Ignore
	public void test() throws UnsupportedEncodingException {
		Directory dir = new Directory("C:\\Users");
		JSONArray ja = dir.gen(new String[] { "C:\\Users" });
		log.info(ja);
	}

	@Test
	public void encodePath() {
		String dir = Directory.encodePath("\\Bing-wallpaper\\Number2_ZH-CN12009255937_1920x1080.jpg");
		log.info(dir);
	}

	@Test
	public void testFormat() {
		log.info(String.format("%-3.3s]", "ab"));
	}

}
