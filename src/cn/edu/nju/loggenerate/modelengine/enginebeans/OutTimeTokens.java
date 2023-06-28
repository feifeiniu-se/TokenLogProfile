/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginebeans;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LiChuanyi
 */
public class OutTimeTokens {

	private static Map<Integer, List<Token>> outTimeTokenMap;

	public static void initiallize() {
		outTimeTokenMap = new HashMap<Integer, List<Token>>();
		Token.resetID();
	}

	public static void setUnhandles() {
		Object[] keys = outTimeTokenMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			List<Token> tokensCase = outTimeTokenMap.get((Integer)keys[i]);
			for (Token t : tokensCase) {
				t.handled = false;
			}
		}

	}

	public static void addToken(Token t) {
		// t.print();
		if (outTimeTokenMap.containsKey(t.getCaseid())) {
			outTimeTokenMap.get(t.getCaseid()).add(t);
		} else {
			List<Token> caseTokens = new ArrayList<Token>();
			caseTokens.add(t);
			outTimeTokenMap.put(t.getCaseid(), caseTokens);
		}
	}

	public static String printALL() {
		String tokenString = "";
		Object[] keys = outTimeTokenMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			List<Token> tokensCase = outTimeTokenMap.get((Integer)keys[i]);
			for (Token tmp : tokensCase) {
				String producer = tmp.getProducer() >= 0 ? TransitionList
						.getByID(tmp.getProducer()).getName() : PlaceList
						.getStartPlace().getName();
				String consumer = tmp.getConsumer() >= 0 ? TransitionList
						.getByID(tmp.getConsumer()).getName() : PlaceList
						.getOverPlace().getName();
				// System.out.println(tmp.getTokenID() + "," + producer
				// + "," + tmp.getProduceTime() + "," + consumer
				// + "," +tmp.getConsumeTime());
				tokenString = tokenString + "<p>" + tmp.getCaseid() + ","
						+ tmp.getTokenID() + "," + producer + ","
						+ tmp.getProduceTime() + "," + consumer + ","
						+ tmp.getConsumeTime() + "</p>";
			}
		}
		return tokenString;
	}

	public static String getLog() {
		String tokenString = "";
		Object[] keys = outTimeTokenMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			List<Token> tokensCase = outTimeTokenMap.get((Integer)keys[i]);
			for (Token tmp : tokensCase) {
				int id = tmp.getTokenID();
				String producer = tmp.getProducer() >= 0 ? TransitionList
						.getByID(tmp.getProducer()).getName() : PlaceList
						.getStartPlace().getName();
				String consumer = tmp.getConsumer() >= 0 ? TransitionList
						.getByID(tmp.getConsumer()).getName() : PlaceList
						.getOverPlace().getName();
				tokenString = tokenString + tmp.getCaseid() + ","
						+ tmp.getTokenID() + "," + producer + ","
						+ tmp.getProduceTime() + "," + consumer + ","
						+ tmp.getConsumeTime() + "\r\n";
			}
		}
		return tokenString;
	}

	public static List<Token> getByCaseID(Integer caseID) {
		return outTimeTokenMap.get(caseID);
	}
	
	public static boolean hasCase(Integer caseID){
		return outTimeTokenMap.containsKey(caseID);
	}
	
	public static List<Token> getAll(){
		List<Token> tokens = new ArrayList<Token>();
		Object[] keys = outTimeTokenMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			List<Token> tokensCase = outTimeTokenMap.get((Integer)keys[i]);
			tokens.addAll(tokensCase);
		}
		return tokens;
	}
}
