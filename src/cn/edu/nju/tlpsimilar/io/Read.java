package cn.edu.nju.tlpsimilar.io;


import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.*;


public class Read {

	//构造HashMap<List<Token>, String>由于HashMap中的key不能重复，所以将同一case的所有token存储为list形式
	//然后将该list存为HashMap的key，每个case中包含的set<Pro,Con>
	public static HashMap<List<Token>, Set<String>> distinctRead(List<Token> tokenLog){
		List<Token> caseList = new ArrayList<Token>();
		Set<String> PCSet = new TreeSet<String>();
		HashMap<List<Token>, Set<String>> hashMap = new HashMap<List<Token>, Set<String>>();
		String currentCaseID = "0";
		Token token;
		for (Token tokenLCY : tokenLog) {
			if(currentCaseID.equals(tokenLCY.getCaseid() + "")){//表示当前case的log还没有读完
				caseList.add(tokenLCY);////list存储的是一个case的log
				PCSet.add(TransitionList.getByID(tokenLCY.getProducer()).getName()+","+TransitionList.getByID(tokenLCY.getConsumer()).getName());//PCSet存储的是一个case的<Pro, Con>的关系对
			}else {//表示当前 caseID不等于上一次的ID，上一个case已经读取完毕，开始读取新的case
				if(hashMap.containsKey(caseList)){
					//如果已经存在了，没有操作;
				}else{//如果不存在该caseList<Token>,那么就存入HashMap
					//判断PCSet是否已经存在，如果PCSet已经存在，那么判断key的长度，去除较长的或者相等的（如果key较长，那么说明是循环节多，相等应该是并发导致的）
					if(hashMap.containsValue(PCSet)){
						List<Token> key = getKeyByValue(hashMap, PCSet);
						if(key.size()>caseList.size()){
							hashMap.remove(key);
							hashMap.put(caseList, PCSet);//移掉长的caselist
						}
					}else if(!caseList.isEmpty())
						hashMap.put(caseList, PCSet);
				}
				currentCaseID = tokenLCY.getCaseid() + "";//更新currentCaseID为当前case的ID
				caseList = new ArrayList<Token>();//记得清空一下caseList
				PCSet = new TreeSet<String>();
				PCSet.add(TransitionList.getByID(tokenLCY.getProducer()).getName()+","+TransitionList.getByID(tokenLCY.getConsumer()).getName());
				caseList.add(tokenLCY);
			}
		}

//          用来判断最后一个case
		if(!hashMap.containsKey(caseList)){
			//如果已经存在了，那么就不操作,如果不存在，则加入
			if(hashMap.containsValue(PCSet)){
				List<Token> key = getKeyByValue(hashMap, PCSet);
				if(key.size()>caseList.size()){
					hashMap.remove(key);
					hashMap.put(caseList, PCSet);//移掉长的caselist
				}
			}else if(!caseList.isEmpty())
				hashMap.put(caseList, PCSet);
		}

		remove0Loop(hashMap);//将hashmap中循环次数为0的去掉，剩下只包含遍历了所有循环的case
		return hashMap;
	}

	private static void remove0Loop(HashMap<List<Token>, Set<String>> hashMap) {
		List<List<Token>> removeKey= new ArrayList<>();//存储待移除的key，最后一并移除
		for(Map.Entry<List<Token>, Set<String>> e:hashMap.entrySet()){
			for(Map.Entry<List<Token>, Set<String>> entry : hashMap.entrySet()){
				if(!e.getKey().equals(entry.getKey())){//当两个的key不相同时，判断两个value是否存在包含关系
					if(e.getValue().containsAll(entry.getValue())){
						if(!removeKey.contains(entry.getKey())){
							removeKey.add(entry.getKey());
						}
					}else if(entry.getValue().containsAll(e.getValue())){
						if(!removeKey.contains(e.getKey())){
							removeKey.add(e.getKey());
						}
					}
				}
			}
		}
		for(List<Token> rk:removeKey){
			hashMap.remove(rk);
		}
	}

	private static List<Token> getKeyByValue(HashMap<List<Token>, Set<String>> hashMap, Set<String> PCSet) {
		Iterator<Map.Entry<List<Token>, Set<String>>> entryKeyIterator = hashMap.entrySet().iterator();
		while (entryKeyIterator.hasNext()) {
			Map.Entry<List<Token>, Set<String>> e = entryKeyIterator.next();
			if(e.getValue().equals(PCSet)){
				return e.getKey();
			}
		}
		return null;
	}

}
