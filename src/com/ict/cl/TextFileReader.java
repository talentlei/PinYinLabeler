package com.ict.cl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class TextFileReader {
	
	public static void main(String[] args) {
		
		
		String file="data/app_name_pak.txt";
//		String file2="data/app_name_pak.gz";
		List<String> result=TextFileReader.readLines(file);
		
		System.out.println("ok" + result);
		
//		List<String> list=readZipLines(file2);
//		for (String string : list) {
//			System.out.println(string);
//		}
	}
	
	public static List<String> readZipLines(String zipFileName){
			
		List<String> arrayList = new ArrayList<String>();
		
		BufferedReader bufferedReader=null;
		try {
			InputStream fileStream = new FileInputStream(zipFileName);
			InputStream gzipStream = new GZIPInputStream(fileStream);
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			bufferedReader = new BufferedReader(decoder);
			
			String arrayListString = null;
			
			while ((arrayListString = bufferedReader.readLine())!=null){
				if(arrayListString.equals("")) continue;
				arrayList.add(arrayListString);
//					System.out.println(arrayListString);
			}
			bufferedReader.close();
	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (bufferedReader!=null) bufferedReader.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
				
		}
		return arrayList;
	}
	
	public static HashSet<String> readUniqueLines(InputStream is) {
		
		HashSet<String> terms = new HashSet<String>();
		if(is==null) return terms;

		BufferedReader bufferedReader = null;
		try {

			bufferedReader = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String str = null;
		
			while ((str = bufferedReader.readLine()) != null) {
			
				if (!str.trim().equals("")&& !str.startsWith("#") && !terms.contains(str)) {
					terms.add(str);
				}
			}
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if (bufferedReader != null)  bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		

		}

		return terms;
	}

	
	
	public static List<String> readLines(String fileName) {
		List<String> arrayList = new ArrayList<String>();
		
		File file = new File(fileName);
		InputStream is = null;
				
		BufferedReader bufferedReader = null;
		try {
			if (!file.exists()) {
	            is = TextFileReader.class.getClassLoader().getResourceAsStream(fileName);	            
	        } else {	           
	            is = new FileInputStream(file);	             
	        }
			try{
				InputStreamReader isr=new InputStreamReader(is,"utf-8");
				bufferedReader = new BufferedReader(isr);
			}catch(NullPointerException npe){  //文件不存在
			    npe.printStackTrace();
				return arrayList;
			}
			
						
			String line = null;
		
			while ((line = bufferedReader.readLine())!=null){
				if(line.trim().equals("") || line.startsWith("#")) continue;
				arrayList.add(line);

			}
			
			bufferedReader.close();
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();			
		} catch (IOException e) {		
			e.printStackTrace();
		}finally{
			if (bufferedReader!=null){
				try {
					bufferedReader.close();
				} catch (IOException e) {					
					e.printStackTrace();					
				}
			}
		}
		return arrayList;
		
	}
	
	

	/**
	 * 去空行，去重，忽略“#”开头的行
	 * @param fileName
	 * @return
	 */
	public static Set<String> readUniqueLines(String fileName) {
			
		File file = new File(fileName);
		InputStream is = null;				
	
		if (!file.exists()) {
            is = TextFileReader.class.getClassLoader().getResourceAsStream(fileName);	            
        } else {	           
            try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {					
				e.printStackTrace();
			}	             
        }
		
		return readUniqueLines(is);
	}
}
