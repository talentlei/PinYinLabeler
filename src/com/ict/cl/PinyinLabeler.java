package com.ict.cl;



import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;



/**
 * 该类用于对字符串进行注音，暂时未支持对英文进行注音，如需对英文注音，可将一些常见音加入到词表pinyin_simp.txt中
 * @author chenlei
 *
 */

public class PinyinLabeler {
//	static final Logger log=LoggerUtil.getLogger(PinyinLabeler.class);
	
	private static Map<String , Double> familyname_dic = new HashMap<String,Double>();
	private static Map<String , Double> secondname_dic = new HashMap<String,Double>();
	private static Map<String , HashMap<String,Float>> pinyin_sys_dic = new HashMap<String,HashMap<String,Float>>();;
	private static Map<String , HashMap<String,Integer>> pinyin_dic = new HashMap<String,HashMap<String,Integer>>();
	//初始化，加载词表内容
	static{
		loadDic();
	}
	
	/**
	 * 加载注音所需要的字典
	 * @throws IOException 
	 */
	public static void loadDic(){
		
		//load familyname dic
		String famliynamepath = "data/pinyin/familyname.txt";
		List<String> lines=TextFileReader.readLines(famliynamepath);
		for (String line : lines) {
			line = line.trim();
			if(line.length() <1) continue;
			//line 张
			if(!familyname_dic.containsKey(line))
				familyname_dic.put(line, (double) 1);
		}

		
		//load secondname dic
		String secondnamepath ="data/pinyin/people_secondname.txt";
		String [] lineSplit;
		lines=TextFileReader.readLines(secondnamepath);
		for (String line : lines) {
			line = line.trim();
			if(line.length() <1) continue;
			//line 鑫灿	2
			lineSplit = line.split("[ \t]");
			if(!secondname_dic.containsKey(lineSplit[0]))
				secondname_dic.put(lineSplit[0], (double) 1);
		}
		
				
		//load pinyin_sys_dic
		String ime_sys ="data/pinyin/ime_sys_dict_newword.dict";
		lines=TextFileReader.readLines(ime_sys);
		for (String line : lines) {
			line = line.trim();
			if(line.length() <1) continue;
			//line  大家 1105413.0 0 da jia
			lineSplit = line.split("[ \t]+");
			String key = lineSplit[0];
			String freq1 = lineSplit[1];
			String py_str = "";
			int i = 3;
			//获取拼音
			while(i < lineSplit.length){
				if(py_str.length() < 1)
					py_str = lineSplit[i];
				else 
					py_str += " "+ lineSplit[i];
				i++;
				//如果dic中还不包含key 创建词典 key为pinyin 
				
			}
			if(!pinyin_sys_dic.containsKey(key)){
				HashMap<String,Float> map =  new HashMap<String,Float>();
				map.put(py_str, Float.valueOf(freq1));
				pinyin_sys_dic.put(key, map);
			}
			else{
				//有问题吗？
				pinyin_sys_dic.get(key).put(py_str, Float.valueOf(freq1));
			}	
			
		}
			
				
		//load pinyin_dic
		String pinyin_simp ="data/pinyin/pinyin_simp.txt";
		lines=TextFileReader.readLines(pinyin_simp);
		for (String line : lines) {
			line = line.trim();
			if(line.length() <1) continue;
			//line  哪      88 Na, 11 Ne
			lineSplit = line.split(", ");
			String  [] keySplit = lineSplit[0].split("[ \t]+");
			String key = keySplit[0];
			String p1 = keySplit[1];
			String py1 = keySplit[2];
			
			if(!pinyin_dic.containsKey(key)){
				HashMap<String,Integer> map = new HashMap<String,Integer>();
				map.put(py1, Integer.valueOf(p1));
				pinyin_dic.put(key, map);
			}
			int i = 1;
			//处理多个读音
			while(i < lineSplit.length){
				String [] freqSplit = lineSplit[i].split("[ \t]+");
				String p2 = freqSplit[0];
				String py2 = freqSplit[1];
				pinyin_dic.get(key).put(py2, Integer.valueOf(p2));
				i++;
			}
		}
	
	}
	/**
	 * 该方法接受一个汉字串，返回该汉字串对应的拼音串  该串可以包含空格
	 * warning:暂时未处理标点，只是简单的删除掉了
	 * @param text 汉字串
	 * @return 拼音串
	 */
	public static String getPinyin(String text){
		text = text.trim();
		if(text.length() < 1)
			return "";
//		text = text.replaceAll("[,，。？！；“：?!;:]", "");
		String [] splitline = text.split("[ \t]+");
		
		String pinyin = "";
		int start = -1;
		for(int i = 0; i < splitline.length; i++){
			pinyin +=labelPY(splitline[i])+" ";
			while(pinyin.contains("_##_")){
				start = pinyin.indexOf("_##_");
				if(pinyin.substring(start + 4).contains("_##_"))
					pinyin = pinyin.substring(0,start) + pinyin.substring(start + 4);
				else
					pinyin = pinyin.substring(0,start) +" "+ pinyin.substring(start + 4);
			}
			;
		}
		
		
		//将 bull  doctor中的多余空格替换为一个空格
		return pinyin.trim().replaceAll("  ", " ");
		
	}
	
	/**
	 * 该方法接受一个汉字串，返回该汉字串对应的拼音串  该串不能包含空格
	 *
	 * @param text 汉字串
	 * @return 拼音串
	 */
	private static String labelPY(String text){
	//	String [] pinyin;
		String [] linetemp;
		Vector<String > lineSplit = new Vector<String>();
		text =text.trim();
		if(text.length() < 1)
			return "";
		linetemp = text.split("[ \t]+");
		int length = linetemp.length;
		
		lineSplit = new Vector<String>();
		for(int i=0; i<length; i++)
			lineSplit.add(linetemp[i]);
		//稍后考虑
		if(length < 2)
			lineSplit.add(""+3000);
		else lineSplit.add(""+3000);

		 if(pinyin_dic.containsKey(lineSplit.get(0))){
			   HashMap<String,Integer> map = pinyin_dic.get(lineSplit.get(0));
			   
			   //获得最有可能的读音
		   String key = "", multpy = "";
		   Integer value = 0, maxweight = -1;
		   Iterator<String> iter = map.keySet().iterator();
		   while(iter.hasNext()){
			   key = iter.next();
			   value = map.get(key);
			   if(value > maxweight){
				   maxweight = value;
				   multpy = key;
		    }
		   }
		   
		   float freq = ((float)(value)/(float)100)*Float.valueOf(lineSplit.get(1));
		   if(freq < 1)
			   freq = 10;
		   String ptmp = "";
		   String py_tmp ="";
		   for(int num =0; num <multpy.length(); num++){
			   char syl = multpy.charAt(num);
		    // 就是a吧 判断是否为新的拼音 拼音首字母是大写的
			   if(syl < 'a'){
				   if(ptmp.length() > 0){
					   ptmp = ptmp.toLowerCase() ;
					   if(py_tmp.length() == 0)
						   py_tmp = ptmp;
					   else 
						   py_tmp +=" "+ptmp;
				   }
				   ptmp = syl +"";
				   continue;
			   }
			   ptmp += syl+"" ; 
		    
		   }
		   if(ptmp.length() > 0){
			   ptmp =ptmp.toLowerCase();
			   if(py_tmp.length() ==0 )
				   py_tmp = ptmp;
			   else 
				   py_tmp += " "+ptmp;
		   }
		   return py_tmp ;
		}else if(pinyin_sys_dic.containsKey(lineSplit.get(0))){
			int total = 1;
			
			HashMap<String,Float> map = pinyin_sys_dic.get(lineSplit.get(0));
			Iterator<String> iter = map.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				total += map.get(key);
			}
			
			iter = map.keySet().iterator();
			while(iter.hasNext()){
				String key = (String)iter.next();
				float value = map.get(key);
				float freq = ((float)(value)/(float)total)*Float.valueOf(lineSplit.get(1));
				if(freq < 1)
					freq = 10;
				
				return key ;
				
			}
		}else if(!pinyin_dic.containsKey(lineSplit.get(0))){
			Vector<String> fmm_list = FMM2(pinyin_dic,lineSplit.get(0));
			Vector<String> bmm_list = BMM2(pinyin_dic,lineSplit.get(0));
			String py_tmp = getSegPinYin(segment(fmm_list,bmm_list));
			
			return py_tmp;

			
		}
		//解决英文问题  如：cba总决赛  c无法匹配上 则对ba总决赛标注，同理以此类推。  加入##$$是为了对cba和正常字符串间分开
		//防止出现这种情况cbake xue yuan
		return text.charAt(0)+"_##_"+labelPY(text.substring(1)).trim();
		
	}
	
	//前向算法分词
	private static Vector<String> FMM2( Map<String , HashMap<String,Integer>> seg_dict, String  phrase){
		int maxlen = 16;
		Vector<String> fmm_list = new Vector<String>();
		int len_phrase = phrase.length();
		int i=0,j=0;
		
		while(i < len_phrase){
			int end = i+maxlen;
			if(end >= len_phrase)
				end = len_phrase;
			String phrase_sub = phrase.substring(i, end);
			for(j = phrase_sub.length(); j >=0; j--){
				if(j == 1)
					break;
				String key =  phrase_sub.substring(0, j);
				if(seg_dict.containsKey(key)){
					fmm_list.add(key);
					i +=key.length() -1;
					break;
				}
			}
			if(j == 1)
				fmm_list.add(""+phrase_sub.charAt(0));
			i+=1;
		}
		return fmm_list;
	}
	
	//后向算法分词
		private static Vector<String> BMM2( Map<String , HashMap<String,Integer>> seg_dict, String  phrase){
			int maxlen = 16;
			Vector<String> bmm_list = new Vector<String>();
			int len_phrase = phrase.length();
			int i=len_phrase,j=0;
			
			while(i > 0){
				int start = i - maxlen;
				if(start < 0)
					start = 0;
				String phrase_sub = phrase.substring(start, i);
				for(j = 0; j < phrase_sub.length(); j++){
					if(j == phrase_sub.length()-1)
						break;
					String key =  phrase_sub.substring(j);
					if(seg_dict.containsKey(key)){
						bmm_list.insertElementAt(key, 0);
						i -=key.length() -1;
						break;
					}
				}
				if(j == phrase_sub.length() -1)
					bmm_list.insertElementAt(""+phrase_sub.charAt(j), 0);
				i -= 1;
			}
			return bmm_list;
		}
		
		/**
		 * 该方法结合正向匹配和逆向匹配的结果，得到分词的最终结果
		 * @param FMM2 正向匹配的分词结果
		 * @param BMM2 逆向匹配的分词结果
		 * @param return 分词的最终结果
		 */
		public static Vector<String> segment(Vector<String> FMM2,Vector<String>BMM2){
			//如果正反向分词结果词数不同，则取分词数量较少的那个
			if(FMM2.size() != BMM2.size()){
				if(FMM2.size() > BMM2.size())
					return BMM2;
				else return FMM2;
			}
			//如果分词结果词数相同
			else{
				//如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
				int i ,FSingle = 0, BSingle = 0;
				boolean isSame = true;
				for(i = 0; i < FMM2.size();  i++){
					if(!FMM2.get(i).equals(BMM2.get(i)))
						isSame = false;
					if(FMM2.get(i).length() ==1)
						FSingle +=1;
					if(BMM2.get(i).length() ==1)
						BSingle +=1;
				}
				if(isSame)
					return FMM2;
				else{
					//分词结果不同，返回其中单字较少的那个
					if(BSingle > FSingle)
						return FMM2;
					else return BMM2;
				}
			}
		}
		
		/**
		 * 该方法用于对分好词的vector<String>注音并格式化拼音
		 * @param seg 分词结果，保存在vector<String>中
		 * @return 返回注音结果
		 */
		public static String getSegPinYin(Vector <String> seg_list){
			
			String bmm_py ="";
			boolean ischar = false;   //标志是否是一个字符
			for(int i = 0; i < seg_list.size(); i++){
				String bmm = seg_list.get(i);
				if(!pinyin_dic.containsKey(bmm)){
					if(ischar == true){
						bmm_py +=bmm;
					}
					else{ 
						bmm_py +=bmm.toUpperCase();
					}
					
					ischar = true;
					continue;
				}
				if(ischar == true){
					ischar = false;
				}
				
				//获取字最可能的读音
				String multpy = "";
				int maxweight = -1;
				Map<String,Integer> map = pinyin_dic.get(bmm);
				Iterator<String> it = map.keySet().iterator();
				while(it.hasNext()){
					String key = (String) it.next();
					int value = map.get(key);
					if(value > maxweight){
						maxweight = value;
						multpy = key;
					}
				}
				bmm_py += multpy;
				
			}
			
			//将pinyin格式化  如：我是歌手  WoShi GeShou 格式化为 wo shi ge shou
			String ptmp ="";
			String py_tmp ="";
			
			for(int i = 0; i < bmm_py.length(); i++){
				char syl = bmm_py.charAt(i);
				if(syl < 'a'){
					if(ptmp.length() > 0){
						ptmp = ptmp.toLowerCase();
						if(py_tmp.length() == 0)
							py_tmp = ptmp;
						else
							py_tmp += " "+ ptmp;
					}
					ptmp = syl +"";
					
					continue;
				}
				ptmp += syl;
					
			}
			if(ptmp.length() >0){
				ptmp = ptmp.toLowerCase();
				if(py_tmp.length() == 0)
					py_tmp =ptmp;
				else 
					py_tmp +=" "+ptmp;
			}
			
				return  py_tmp; 
		}
		
		
//		public static void main(String [] args) throws IOException{
//		
//			String[] texts=new String[]{
//					"拿嘛正转",
//					 "我喜欢吃人参和海参，去参加会议",
//					"我是doctor是真的",
//					 "nba",
//					 "传奇这首歌真好听",
//					 
//					
//			};
//			for (String text : texts) {
//				System.out.println(text+"-->"+PinyinLabeler.getPinyin(text));
////				System.out.println(text+"-->"+PinyinLabeler.getPinyin(text));
//			}
//			
//			
//			
//		}
//		
//		public static void main(String [] args) throws IOException{
//			for (String text : args) {
//				System.out.println(text+"-->"+PinyinLabeler.getPinyin(text));
//			}
//		}
		
}


