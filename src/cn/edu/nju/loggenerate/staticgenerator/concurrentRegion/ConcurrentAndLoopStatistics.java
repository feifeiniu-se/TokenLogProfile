package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

public class ConcurrentAndLoopStatistics {
    public static float finalTraces = 0;

    public static float totalAveragePathLength = 0;
    public static float numberOfTraces = 1;
    public static int loops = 0;

    public static void initialize(){
        totalAveragePathLength = 0;
        numberOfTraces = 1;
        loops = 0;
    }

    public static long sum(int num){
        //ע��20����ֵ�Ѿ�����int���ֵ���ʷ���ֵӦΪlong�ͣ������int�ʹ洢������λȡ�����int�͵ĸ���
        if(num==1){
            return 1;
        }
        else{
            return num*sum(num-1);
        }
    }
}
