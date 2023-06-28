package cn.edu.nju.tlpsimilar.extractRelations;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.HashMap;
import java.util.List;

import static cn.edu.nju.tlpsimilar.extractRelations.GetRelations.putRelation;

public class GetSequence {
    public static void getSequence(List<Token> tokenList, HashMap<HashMap<String, String>, String> relations){
        //tokenlist�����е�˳��ṹ�洢
        for(Token t:tokenList){
            putRelation(t, relations, "S");
        }

    }
}
