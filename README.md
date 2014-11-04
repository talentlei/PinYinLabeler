PinYinLabeler
=============
Welcome to my home!
This is a program of labeler pinyin for Chinese Character.

汉字注音


思想

汉字注音程序的思想主要是依赖一些已有的常用词的注音词表。对于给定的需要注音的汉字串text，首先对text进行分词。这里的分词方法采用了正向最大匹配和逆向最大匹配想结合的方法。然后根据注音词表中每个词是某个音的概率，得到一个最大可能的注音结果。

思想很简单，实现后发现结果还不错。为了应用方便，我将程序打了jar包，PYLabeler_v1.0.jar.

调用方法：

import com.ict.cl.PinyinLabeler;


public class Laber {

	public static void main(String [] args){
	
		String text = "参加会议";
		System.out.println(text+"-->"+PinyinLabeler.getPinyin(text));

	}

}

参加会议-->can jia hui yi

灰常简单，欢迎使用。
