package hf.wj.dir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSONfy Directory file list.
 *
 * @author Asparagus
 * @version $Id: $Id
 */
public class Directory {
	/**
	 * The class log instance.
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
	 */
	public JSONArray gen(String[] args) {
		String path = ".";
		if (args != null && args.length > 0) {
			path = args[0];
		}
		this.fja = this.traversal(path, false);
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
	 * @param path  String
	 * @param state boolean
	 * @return jsonArray JSONArray
	 */
	protected JSONArray traversal(String path, boolean state) {
		JSONArray ja = new JSONArray();
		if (path == null)
			return ja;
		File f = new File(path);
		if (f == null || f.exists() == false || f.isDirectory() == false) {
			System.out.println(path + " not exist.");
			return ja;
		}
		File[] files = f.listFiles();
		if (files != null) {
			for (File fs : files) {
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
						String _base = toPath(this.baseDir);
						new JSONObject();
						jo.put("a_attr", JSONObject.fromObject(
								"{\"href\":\"" + encodePath(toPath(fs.getPath())).replace(_base, "") + "\"}"));
						jo.put("attr", JSONObject
								.fromObject("{\"href\":\"" + toPath(fs.getPath()).replace(_base, "") + "\"}"));
					} else {
						jo.put("a_attr",
								JSONObject.fromObject("{\"href\":\"" + encodePath(toPath(fs.getPath())) + "\"}"));
						jo.put("attr", JSONObject.fromObject("{\"href\":\"" + toPath(fs.getPath()) + "\"}"));
					}
				}
				ja.add(jo);
			}
		}
		return ja;
	}

	/**
	 * 特殊字符+ 转url encode
	 *
	 * @param path String uri
	 * @return encoded path
	 */
	public static String encodePath(String path) {
		String upath = null;
		try {
			URI u = new URI(null, null, path);
			upath = u.toASCIIString().replaceAll("\\+", "%20");
			return upath.replace("#", "");
		} catch (URISyntaxException e) {
			LOG.error(e);
		}
		return null;
	}

	/**
	 * 路径转换: Windows 路径转Uri
	 */
	private static String toPath(String path) {
		return path == null ? null : path.replace("\\", "/");
	}
}
