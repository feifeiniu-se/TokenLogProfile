package cn.edu.nju.tlpsimilar.similarity;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import cn.edu.nju.tlpsimilar.io.Write;
import cn.edu.nju.tlpsimilar.constructMatrix.Matrix;
import cn.edu.nju.tlpsimilar.constructMatrix.UniformDimention;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Similarity {

//	//���ƶȶԱ�
//	public static double relations_similarity(Set<R> a, Set<R> b){
//		double sim = 0;
//		int total = a.size() + b.size();
//
//		Set<R> temp = new TreeSet<R>();
//		temp.addAll(a);
//		temp.retainAll(b);
//		System.out.println(temp.size()*2 + " / " + total);
//		sim = (double)(temp.size()*2) / total;//Ϊ�˱������relations�Ĺ�ϵ������ʱ����temp
//		return sim;
//	}
	
	public static float TLP_similarity(HashMap<HashMap<String, String>, String> a, HashMap<HashMap<String, String>, String> b){
		float sim = 0;
		String[][] m1 = Matrix.getMatrix(a);
		String[][] m2 = Matrix.getMatrix(b);

		System.out.println();
		System.out.println("(1)TLP");
		Write.print_matrix(m1);
		System.out.println("(2)TLP");
		Write.print_matrix(m2);
		System.out.println();
		
		
		String[][] uMatrix = UniformDimention.union(m1, m2);
		m1 = UniformDimention.transform(m1, uMatrix);
		m2 = UniformDimention.transform(m2, uMatrix);
		
//		System.out.print("(1)ͬά�ɱȽ�TLP");
//		Write.print_matrix(m1);
//		System.out.print("(2)ͬά�ɱȽ�TLP");
//		Write.print_matrix(m2);
		int total=0;
		int sum=0;
		for(int i=1; i<m1.length; i++){
			for(int j=1; j<m1[0].length; j++){
				if(m1[i][j] != ""){
					String[] s1 = m1[i][j].split(",");
					total += s1.length;
					if(m2[i][j] != ""){
						String[] s2 = m2[i][j].split(",");
						total += s2.length;
						for(int k=0; k<s1.length; k++){
							for(int f=0; f<s2.length; f++){
								if(s1[k].equals(s2[f])){
									sum += 2;
								}
									
							}
						}
					}
				}else if(m2[i][j] != ""){
					String[] s2 = m2[i][j].split(",");
					total += s2.length;
				}
				
			}
		}

		sim = (float) sum / total;
		return sim;
	}
	
}
