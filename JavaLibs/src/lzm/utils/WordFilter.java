package lzm.utils;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 算法思路：把敏感词的第一个字符取出来，作为比较对象。
 * 遍历整个字符串，如果发现字符跟敏感词第一个字符相同，
 * 就从字符串取出跟关键词相同长度的子串比较，如果相同就替换
 * 
 * 本算法比较适合敏感词都不长的场合
 */
public class WordFilter {
	
	private static Map<Character,List<String>> wordMap;
	private static boolean isInit = false;
	
    private static void wordListToMap(List<String> sensitiveWordList){
    	wordMap = new HashMap<Character,List<String>>();
        for (String s:sensitiveWordList){
            char c=s.charAt(0);
            List<String> strs = wordMap.get(c);
            if (strs==null){
                strs=new ArrayList<String>();
                wordMap.put(c,strs);
            }
            strs.add(s);
        }
    }
    
    public static void init(String filePath){
    	if(isInit) return;
    	isInit = true;
    	
//    	File file = new File(filePath);
		BufferedReader reader = null;
		List<String> wordList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"utf-8"));//new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null){
				String []str = tempString.split("    ");
				if(str[0].equals("3")){
					wordList.add(str[1]);
				}
			}
			wordListToMap(wordList);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
    	
    }
    
    public static String filter(String src){
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < src.length() ; i++){
            char c = src.charAt(i);
            String find = null;
            if (wordMap.containsKey(c)){
                List<String> words=wordMap.get(c);
                for (String s:words){
                    String temp = src.substring(i,(s.length() <= (src.length()-i)) ? i+s.length() : i);
                    if (s.equals(temp)){
                        find = s;
                        break;
                    }
                }
            }
            if (find != null){
                strb.append("*");
                i += (find.length() - 1);
            } else {
                strb.append(c);
            }
        }
        return strb.toString();
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
    	
    	init("/Users/lingjing/projects/sgz/腾讯独代合作/安全信息接入/vocabulary.txt");
    	
        
        long time = System.currentTimeMillis();
        
//        for (int i = 0; i < 10000; i++) {
        	System.out.println(filter("你新疆维瘟甲爆族人&畜生，也温家宝&影帝太缺稳夹宝德了TMD王立军吧"));
//		}
        
        System.out.println(System.currentTimeMillis()-time);
    }

}

