/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.flowbeans;

import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;

/**
 *
 * @author LiChuanyi
 */
public class Token {
    
    private static int id = 1;
    public static void resetID(){
    	id = 1;
    }
    
    private int tokenID;
    
    private int produceTime;
    private int consumeTime;
    private int caseid;
    
    private int producer = -2;
    private int consumer = -1;
    
    private boolean isConsumed = false;
    
    public boolean handled = false;
    
    public Token(){
        this.tokenID = id;
        id ++;
    }

    public int getTokenID() {
        return tokenID;
    }

    public void setTokenID(int tokenID) {
        this.tokenID = tokenID;
    }

    public int getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(int produceTime) {
        this.produceTime = produceTime;
    }

    public int getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(int consumeTime) {
        this.consumeTime = consumeTime;
    }

    public int getProducer() {
        return producer;
    }

    public void setProducer(int producer) {
        this.producer = producer;
    }

    public int getConsumer() {
        return consumer;
    }

    public void setConsumer(int consumer) {
        this.consumer = consumer;
    }

    public boolean isIsConsumed() {
        return isConsumed;
    }

    public void setIsConsumed(boolean isConsumed) {
        this.isConsumed = isConsumed;
    }

	public int getCaseid() {
		return caseid;
	}

	public void setCaseid(int caseid) {
		this.caseid = caseid;
	}

	public void print(){
		System.out.println(this.getCaseid()+":"+this.getTokenID()+":"+this.getProducer()+":"+this.getConsumer());
	}

	public String getProName(){
        return TransitionList.getByID(getProducer()).getName();
    }

    public String getConName(){
        return TransitionList.getByID(getConsumer()).getName();
    }

    public String getProVisibleName(){
        return TransitionList.getByID(getProducer()).getVisibleName();
    }

    public String getConVisibleName(){
        return TransitionList.getByID(getConsumer()).getVisibleName();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token another = (Token) o;

        if (getProName() != null ? !getProName().equals(another.getProName()) : another.getProName() != null) return false;
        if (getProduceTime() != another.getProduceTime()) return false;

        if (getConName() != null ? !getConName().equals(another.getConName()) : another.getConName() != null) return false;

        return (getProduceTime() == another.getProduceTime());
    }

    public int hashCode() {
        int result = getProName().hashCode();
        result = 31 * result + (((Integer)getProduceTime()).hashCode());
        result = 31 * result + (getConName().hashCode());
        result = 31 * result + (((Integer)getConsumeTime()).hashCode());
        return result;
    }

    public String toString(){
        return "{producer: " + getProName() + " produceTime: " + getProduceTime() + " consumer: " + getConName() + " consumeTime: " + getConsumeTime() + "}" ;
    }
}
