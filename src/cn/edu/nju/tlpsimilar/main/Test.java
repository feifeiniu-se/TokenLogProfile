//package cn.edu.nju.loggenerate.tlpsimilar.main;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.tlpsimilar.relation.Merge;
//
//public class Test {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
////		String a[] = {"v", "c"};
////		String b[] = {"d", "s"};
////		int a1 =1;
////		int b1=2;
////		Merge.cycle_Merge(a, b);
////		List<String> a = new ArrayList<String>();
////		a.add("s");
////		a.add("er");
////		System.out.println(a.get(a.size()-1));
//
//		//System.out.println(a[0] + " " + b[0]);
//
//		double [][] result = new double[21][21];
//		for(int i=1; i<=20; i++){
//			for(int j=1; j<=20; j++){
//				String file1 = "D:/test/log/M"+ i +".tlog";
//				String file2 = "D:/test/log/M"+ j +".tlog";
//				result[i][j] = Triangle.main(file1, file2);
//			}
//
//		}
//		DecimalFormat df = new DecimalFormat( "0.00000 ");
//		for(int i=1; i<=20; i++){
//			for(int j=1; j<=20; j++){
//				System.out.println(df.format(result[i][j]));
//
//			}
//
//		}
//
//		for(int i=1; i<=20; i++){
//			for(int j=i+1; j<=20; j++){
//				for(int k=j+1; k<=20; k++){
//					if(result[i][j]+result[i][k]<=result[j][k])
//						System.out.println("chucuole: " + i + " " + j + " " + k + " " + result[i][j] + result[i][k] + result[j][k]);
//					if(result[j][k]+result[i][k]<=result[i][j])
//						System.out.println("chucuole2: " + i + " " + j + " " + k + " " + result[i][j] + result[i][k] + result[j][k]);
//					if(result[j][k]+result[i][j]<=result[i][k])
//						System.out.println("chucuole3: " + i + " " + j + " " + k + " " + result[i][j] + result[i][k] + result[j][k]);
//				}
//			}
//		}
//
//	}
//
//}
