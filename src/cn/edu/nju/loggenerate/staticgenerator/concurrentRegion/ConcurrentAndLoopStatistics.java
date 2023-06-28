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
        //注意20！的值已经超出int最大值，故返回值应为long型，如果用int型存储则会最高位取反变成int型的负数
        if(num==1){
            return 1;
        }
        else{
            return num*sum(num-1);
        }
    }
}
