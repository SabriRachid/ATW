package com.genererRapport;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.turbine.services.servlet.TurbineServlet;
public class FileManager {
	
	private File outDir;

	public FileManager() {
		outDir = new File(TurbineServlet.getRealPath("RH"));
		outDir.mkdirs();
	}

	/**
	 * @return the outDir
	 */
	
	public File getOutDir() {
		return outDir;
	}

	/**
	 * @param outDir
	 *            the outDir to set
	 */
	public void setOutDir(File outDir) {
		this.outDir = outDir;
	}

	/**
	 * Write on file
	 * 
	 * @param fileName
	 *            file name
	 * @param content
	 *            content to write
	 * @param append
	 *            append to file or not
	 */
	
	public void writeFile(String fileName, String content, boolean append) {
		try {
			// we get the output file
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName,append));
			// we write the xml to the file
			out.write(content);
			// we close the writer
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	public Map<String, String> readConfigFile(String fileName) {
		Map<String, String> param = new HashMap<String, String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String strLine;
			while ((strLine = in.readLine()) != null) {
				String[] info = strLine.split("=");
				param.put(info[0], info[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return param;
	}


}
