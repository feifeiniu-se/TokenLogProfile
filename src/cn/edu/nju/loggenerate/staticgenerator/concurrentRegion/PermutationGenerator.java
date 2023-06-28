package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

import java.math.BigInteger;

public class PermutationGenerator {

    private int[] a;
    private BigInteger numLeft;
    private BigInteger total;
    
    public PermutationGenerator(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Min 1");
        }
        a = new int[n];
        total = getFactorial(n);
        reset();
    }

    public void reset() {
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        numLeft = new BigInteger(total.toString());
    }

    public boolean hasMore() {
        return numLeft.compareTo(BigInteger.ZERO) == 1;
    }

    //得到组合的总数
    private static BigInteger getFactorial(int n) {
        BigInteger fact = BigInteger.ONE;
        for (int i = n; i > 1; i--) {
            fact = fact.multiply(new BigInteger(Integer.toString(i)));
        }
        return fact;
    }

    public int[] getNext() {

        if (numLeft.equals(total)) {
            numLeft = numLeft.subtract(BigInteger.ONE);
            return a;
        }

        // Find largest index j with a[j] < a[j+1]
        int j = a.length - 2;
        while (a[j] > a[j + 1]) {
            j--;
        }

        //a[]从右向左，找index k，使得a[k]>a[j]
        int k = a.length - 1;
        while (a[j] > a[k]) {
            k--;
        }
        //a[j]a[k]互换
        interchange(a, j, k);

        //如果j不是倒数第二个index，则将j+1到a结尾的所有元素，对称互换
        int r = a.length - 1;
        int s = j + 1;
        while (r > s) {
        	interchange(a, r, s);
            r--;
            s++;
        }

        numLeft = numLeft.subtract(BigInteger.ONE);
        return a;

    }
    
    private static int[] interchange(int[] arr, int indexA, int indexB) {
    	int temp = arr[indexA];
    	arr[indexA] = arr[indexB];
    	arr[indexB] = temp;
    	return arr;
    }
    
    //测试
    public static void main(String[] args) {

        int[] indices;
        String[] elements = new String[]{"a", "b", "c"};
        PermutationGenerator x = new PermutationGenerator(elements.length);
        StringBuffer permutation;

        while (x.hasMore()) 
        {
            permutation = new StringBuffer();
            indices = x.getNext();
            for (int i = 0; i < indices.length; i++) {
                permutation.append(indices[i]);
            }
            System.out.println(permutation.toString());
        }
    }
}

