package cn.edu.nju.tlpsimilar.constructMatrix;

import java.util.Set;
import java.util.TreeSet;

public class UniformDimention {
	public static String[][] union(String[][] a, String[][] b){
		Set<String> set = new TreeSet<String>();
		for(int i=1; i<a.length; i++){
			set.add(a[0][i]);
		}
		for(int i=1; i<b.length; i++){
			set.add(b[0][i]);
		}

		String[][] uMatrix = new String[set.size()+1][set.size()+1];
		for(int i=0; i<uMatrix.length; i++){
			for(int j=0; j<uMatrix.length; j++){
				uMatrix[i][j]= "";
			}
		}
		int v = 1;
		for(Object o:set){
			uMatrix[0][v] = o.toString();
			uMatrix[v][0] = o.toString();
			v++;
			//System.out.println(hashMap.get(o));
		}
		return uMatrix;
	}
	
	public static String[][] transform(String[][] x, String[][] u){
		String[][] uMatrix = new String[u.length][u[0].length];
		for(int i=0; i<u.length; i++){
			for(int j=0; j<u[i].length; j++){
				uMatrix[i][j] = u[i][j];
			}
		}
		for(int i=1; i<x.length; i++){
			for(int j=1; j<x[i].length; j++){
				int m=1, n=1;
				while(m<uMatrix.length && !x[i][0].equals(uMatrix[m][0]) )
					m=m+1;
				while(n<uMatrix[0].length && !x[0][j].equals(uMatrix[0][n]))
					n=n+1;
				if(m<uMatrix.length && n<uMatrix[0].length)
					uMatrix[m][n] = x[i][j];
			}
		}
		return uMatrix;
	}
}
