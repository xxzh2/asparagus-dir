package hf.wj.dir;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirectoryApplication {
	static final Log LOG = LogFactory.getLog(DirectoryApplication.class);
	static final String DEFAULT_PATH = System.getProperty("user.home") + File.separator + "Documents";

	public static void main(String[] args) {
		String path = null;
		String base = new File(".").getAbsolutePath();
		if (args != null && args.length > 0) {
			path = args[0];
		}

		if (args != null && args.length > 1) {
			base = args[1];
		}

		if (path == null)
			path = DEFAULT_PATH;
		LOG.info("base path: " + path);
		Directory dir = new Directory(base);
		dir.setShowExtend(true);
		try {
			dir.gen(new String[] { path });
		} catch (Exception e) {
			LOG.error(e);
		}
		dir.write(".");
	}

}
