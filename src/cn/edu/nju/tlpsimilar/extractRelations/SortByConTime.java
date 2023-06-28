package cn.edu.nju.tlpsimilar.extractRelations;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.Comparator;

//����������
public class SortByConTime implements Comparator<Token>{
    public int compare(Token t1, Token t2){
        int a1, a2, b1, b2;
        a1=t1.getProduceTime();
        a2=t2.getProduceTime();
        b1=t1.getConsumeTime();
        b2=t2.getConsumeTime();

        if(b1>b2){
            return 1;
        }else if(b1==b2){
            if(a1>a2){
                return 1;
            }else if(a1==a2){
                return 0;
            }else
                return -1;
        }else
            return -1;

        //ע�⣺��߱���д��������������Ų�����
    }
}