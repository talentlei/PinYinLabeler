package com.ict.cl;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class TextFileWriter {
	
	public static void writeGZipFile(String fileName, List<String> arrayList){

		BufferedWriter bufferedWriter = null;
		try {
			FileOutputStream output = new FileOutputStream(fileName);
			
			Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8");
			
			
			bufferedWriter = new BufferedWriter(writer);
			for (String arrayListItem : arrayList) {
				bufferedWriter.write(arrayListItem);
				bufferedWriter.write("\n");
			}
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeLines(String fileName, Collection<String> lines)  {
		
		File file = new File(fileName);
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (String line : lines) {
				bufferedWriter.write(line);
				bufferedWriter.write("\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeLines(String fileName, List<String> lines,boolean appendMode)  {
		if(!appendMode){
			writeLines(fileName,lines);
			return;
		}
		
		
		
		File file = new File(fileName);
//		if(file.exists()){
//			
//		}
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			for (String line : lines) {
				bufferedWriter.write(line);
				bufferedWriter.write("\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeLines(String fileName, Set<String> lines, String encoding){
		BufferedWriter bufferedWriter = null;
		//OutputStreamWriter out=null;
		try {
			bufferedWriter =new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName),encoding));
			for (String line : lines) {
				bufferedWriter.write(line);
				if(!line.endsWith("\n"))  bufferedWriter.write("\n");
			}
			bufferedWriter.close();
			
		} catch (UnsupportedEncodingException e) {		
			e.printStackTrace();
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeLines(String fileName, Set<String> lines) {
		File file = new File(fileName);
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (String line : lines) {
				bufferedWriter.write(line);
				if(!line.endsWith("\n"))  bufferedWriter.write("\n");
			}
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
