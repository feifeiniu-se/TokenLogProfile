package cn.edu.nju.tlpsimilar.io;


import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.*;

public class Random2Complete {
    public static void ran2com(HashMap<List<Token>, Set<String>> tokenListMap){
//        System.out.println(tokenListMap.size());
        Iterator<Map.Entry<List<Token>, Set<String>>> entryKeyIterator = tokenListMap.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Map.Entry<List<Token>, Set<String>> e = entryKeyIterator.next();
            List<Token> key = e.getKey();
            Set<String> value = e.getValue();
//            System.out.println(tokenListMap.containsValue(value));
        }
    }
}
