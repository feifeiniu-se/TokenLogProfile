//package cn.edu.nju.loggenerate.tlpsimilar.token;
//
//public class TokenNFF {
//
//    private String producer;
//    private String consumer;
//    private String proTime;
//    private String conTime;
//
//    //创建一个数据类型<生产者，生产时间，消费者，消费时间>存储每一条记录
//    public TokenNFF(String producer, String proTime, String consumer, String conTime){
//        this.setPro(producer);
//        this.proTime = proTime;
//        this.setConsumer(consumer);
//        this.conTime = conTime;
//    }
//
//    public String getPro(){
//        return producer;
//    }
//    public void setPro(String producer){
//        this.producer = producer;
//    }
//    public String getCon(){
//        return consumer;
//    }
//    public void setCon(String consumer){
//        this.setConsumer(consumer);
//    }
//    public String getProTime(){
//        return proTime;
//    }
//    public void setProTime(String proTime){
//        this.proTime=proTime;
//    }
//    public String getConTime(){
//        return conTime;
//    }
//    public void setConTime(String conTime){
//        this.conTime=conTime;
//    }
//
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        TokenNFF data = (TokenNFF) o;
//
//        if (getPro() != null ? !getPro().equals(data.getPro()) : data.getPro() != null) return false;
//        if (proTime != null ? !proTime.equals(data.proTime) : data.proTime != null) return false;
//        if (getCon() != null ? !getCon().equals(data.getCon()) : data.getCon() != null) return false;
//        return conTime != null ? conTime.equals(data.conTime) : data.conTime == null;
//    }
//
//    public int hashCode() {
//        int result = getPro() != null ? getPro().hashCode() : 0;
//        result = 31 * result + (proTime != null ? proTime.hashCode() : 0);
//        result = 31 * result + (getCon() != null ? getCon().hashCode() : 0);
//        result = 31 * result + (conTime != null ? conTime.hashCode() : 0);
//        return result;
//    }
//
//    public String toString(){
//        return "{producer: " + getPro() + " produceTime: " + proTime + " consumer: " + getCon() + " consumeTime: " + conTime + "}" ;
//    }
//
//    public void setConsumer(String consumer) {
//        this.consumer = consumer;
//    }
//
//
//}
