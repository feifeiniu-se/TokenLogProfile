//package cn.edu.nju.loggenerate.tlpsimilar.main;
//import java.io.File;
//import java.text.DecimalFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//
//import com.tlpsimilar.io.Read;
//import com.tlpsimilar.relation.Concurrent;
//import com.tlpsimilar.relation.Merge;
//import com.tlpsimilar.relation.R;
//import com.tlpsimilar.similarity.Similarity;
////
//public class Triangle {
//
//	//public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		public static double main(String file1, String file2) {
//
//			// TODO Auto-generated method stub
//
//			long start = System.currentTimeMillis();
//
//			File filePath1 = new File(file1);
//			File filePath2 = new File(file2);
//			System.out.println();
//			//���Ȼ��ȥ�غ��case����
//			HashMap<List<TokenNFF>, String> hashMap1 = Read.distinctRead(filePath1);
//			HashMap<List<TokenNFF>, String> hashMap2 = Read.distinctRead(filePath2);
//
//			Set<R> relations1 = new TreeSet<R>();
//			Set<R> relations2 = new TreeSet<R>();
//
//			//ȥ�غ�ɸѡѭ����ϵ,�õ�������Сcase���ϵ�token��ʽ������ѭ���ṹ��û������Ϊ0��1
//			HashMap<Set<R>, String> relations_hashMap1 = new HashMap<Set<R>, String>();
//			HashMap<Set<R>, String> relations_hashMap2 = new HashMap<Set<R>, String>();
//
//			Merge.unDuplicate(hashMap1, relations_hashMap1, relations1);
//			Merge.unDuplicate(hashMap2, relations_hashMap2, relations2);//���relation�д洢��ѭ������
//
//			relations1.addAll(Merge.cycle_Merge(relations_hashMap1));
//			relations2.addAll(Merge.cycle_Merge(relations_hashMap2));//������ѭ����ϵ��ѡ���ϵ��˳���ϵ
//
//			//Write.print_hashMap(hashMap1);
//			Concurrent.concurrent_Relation(hashMap1, relations1);
//			Concurrent.concurrent_Relation(hashMap2, relations2);//�洢������ϵ
//
//
////			Write.print_hashMap(hashMap1);		//�����Сcase����
////			Write.print_hashMap(hashMap2);
////			Write.print_set(relations1);//�����������ϵ
////			System.out.println();
////	        Write.print_set(relations2);
//
//			System.out.println("ģ�ͣ� \"" + filePath1.toString().substring(8) + "\" VS \"" + filePath2.toString().substring(8) + "\"" );
////
//			DecimalFormat df = new DecimalFormat( "0.00000 ");
//			double sim = Similarity.relations_similarity(relations1, relations2);
//			System.out.println("�����ϵ���ƶȽ��Ϊ�� " + df.format(sim));
////
//			double sim_TLP = Similarity.TLP_similarity(relations1, relations2);
//			System.out.println("TLP���ƶȽ��Ϊ�� " + df.format(sim_TLP));
//
//			long end = System.currentTimeMillis();
//			System.out.println((end - start) + "ms");
//			return sim_TLP;
//	}
//
//}
