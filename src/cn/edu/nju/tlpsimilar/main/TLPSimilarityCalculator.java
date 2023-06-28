package cn.edu.nju.tlpsimilar.main;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;
import cn.edu.nju.tlpsimilar.io.Read;


import static cn.edu.nju.tlpsimilar.extractRelations.GetRelations.getRelations;
import static cn.edu.nju.tlpsimilar.similarity.Similarity.TLP_similarity;

public class TLPSimilarityCalculator {

	public static float getSimilarityValue(List<Token> log1, List<Token> log2){

		HashMap<HashMap<String, String>, String> relations1 = new HashMap<HashMap<String, String>, String>();
		HashMap<HashMap<String, String>, String> relations2 = new HashMap<HashMap<String, String>, String>();

		long time1 = System.nanoTime();
		//首先获得去重后的case集合
		HashMap<List<Token>, Set<String>> tokenList1 = Read.distinctRead(log1);
		long time2 = System.nanoTime();
//		System.out.print((time2-time1) + "\t");

		HashMap<List<Token>, Set<String>> tokenList2 = Read.distinctRead(log2);
		//hashmap的key存储case的tokenlist，value存储case的set<Pro,Con>.根据hashmap的key不能重复性质将完全一样的case去除
		long time3 = System.nanoTime();
//		System.out.print((time3-time2) + "\t");

		//		获取所有的特殊关系
		relations1 = getRelations(tokenList1);
		long time4 = System.nanoTime();
//		System.out.print((time4-time3) + "\t");

		relations2 = getRelations(tokenList2);
		long time5 = System.nanoTime();
//		System.out.print((time5-time4) + "\t");

		DecimalFormat df = new DecimalFormat( "0.00000 ");
		float sim_TLP = TLP_similarity(relations1, relations2);
//		System.out.println("TLP相似度结果为： " + df.format(sim_TLP));
		long time6 = System.nanoTime();
//		System.out.print((time6-time5) + "\t");
		return sim_TLP;
	}

}
