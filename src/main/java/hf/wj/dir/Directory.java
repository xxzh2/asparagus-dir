package hf.wj.dir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSONfy Directory file list.
 *
 * @author Asparagus
 *
 */
public class Directory {
	/**
	 * log
	 */
	static final Log LOG = LogFactory.getLog(Directory.class);
	/**
	 * Internal Variable JSON object.
	 */
	private JSONArray fja = null;
	/**
	 * Base directory.
	 */
	private String baseDir = null;

	/**
	 * If leaf-node show file extensions.
	 */
	boolean isWithExtend = false;

	/**
	 * Default Constructor.
	 */
	public Directory() {

	}

	/**
	 * Replace base directory {@link}baseDir{@link} by current directory("./").
	 *
	 * @param baseDir String
	 */
	public Directory(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * Set is show extension.
	 *
	 * @param show boolean
	 */
	public void setShowExtend(boolean show) {
		this.isWithExtend = show;
	}

	/**
	 * Generate JSON data.
	 *
	 * @param args String[]
	 * @return jsonArray JSONArray
	 * @throws UnsupportedEncodingException
	 */
	public JSONArray gen(String[] args) {
		String path = ".";
		if (args != null && args.length > 0) {
			path = args[0];
		}
		try {
			this.fja = this.traversal(path, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.fja;
	}

	/**
	 * Write generate json to file files.json.
	 *
	 * @param path String
	 */
	public void write(String path) {
		String filePath = null;
		if (this.fja != null && path != null && new File(path).exists()) {
			File f = new File(new File(path).getParent(), "files.json");
			filePath = f.getAbsolutePath();
			try {
				if (f.exists())
					f.delete();
				f.createNewFile();
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
				osw.write(this.fja.toString());
				osw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Runtime.getRuntime().exec("attrib " + "\"" + f.getAbsolutePath() + "\"" + " +H");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOG.info("JSON file path: " + filePath);
	}

	/**
	 * Generate sub directory.
	 *
	 * @param path String
	 * @return jsonArray JSONArray
	 * @throws UnsupportedEncodingException
	 */
	protected JSONArray traversal(String path, boolean state) throws UnsupportedEncodingException {
		File f = new File(path);
		if (path == null || f.exists() == false || f.isDirectory() == false) {
			System.out.println(path + " not exist.");
			return null;
		}
		JSONArray ja = new JSONArray();
		if (f.listFiles() != null) {
			for (File fs : f.listFiles()) {
				JSONObject jo = new JSONObject();
				if (fs.isHidden()) {
					continue;
				} else if (fs.isDirectory()) {
					jo.put("text", fs.getName());
					jo.put("children", this.traversal(fs.getPath(), state));
					jo.put("state", "{\"opened\":" + state + ", \"disabled\":false, \"selected\":false}");
				} else {
					String fileName = fs.getName();
					if (fileName != null && fileName.contains(".") && !this.isWithExtend) {
						fileName = fileName.substring(0, fileName.lastIndexOf("."));
					}
					if (fileName.equals("index") || fileName.equals("index.html") //
							|| fileName.equals("index.htm") //
							|| fileName.equals("files.json") //
							|| fileName.equals("files")) {
						continue;
					}
					String fsExt = null;
					if (fileName.contains(".")) {
						fsExt = fileName.substring(fileName.lastIndexOf(".") + 1);
						String fileNameNoExt = fileName.substring(0, fileName.lastIndexOf("."));
						String fullName = String.format("[%-3.3s] %s", fsExt.toLowerCase(), fileNameNoExt);
						jo.put("text", fullName.replace(" ", "&nbsp;"));
					} else {
						jo.put("text", fileName);
					}
					jo.put("icon", "jstree-leaf");
					if (this.baseDir != null) {
						String _base = this.baseDir.replace("\\", "/");
						jo.put("a_attr", "{\"href\":'"
								+ URLEncoder.encode(fs.getPath().replace("\\", "/").replace(_base, "."), "utf-8")
								+ "'}");
					} else {
						jo.put("a_attr",
								"{\"href\":'" + URLEncoder.encode(fs.getPath().replace("\\", "/"), "utf-8") + "'}");
					}
				}
				ja.add(jo);
			}
		}
		return ja;
	}
}
