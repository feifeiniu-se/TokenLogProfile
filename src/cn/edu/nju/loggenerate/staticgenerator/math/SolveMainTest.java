package cn.edu.nju.loggenerate.staticgenerator.math;

import java.util.ArrayList;

public class SolveMainTest {
	public static void main(String[] argv) {
		//tet
		//int[][] a = { {-1,0,1},{1,-1,0},{1,-1,0},{0,1,-1}};
		int[][] a = {
				{-1,0,0,0,0,0,0,0,1},
				{1,-1,-1,0,0,0,0,0,0},
				{1,0,0,-1,0,0,0,0,0},
				{0,1,0,0,-1,0,0,0,0},
				{0,0,1,0,0,-1,0,0,0},
				{0,0,0,1,0,0,-1,0,0},
				{0,0,0,0,1,1,0,-1,0},
				{0,0,0,0,0,0,1,-1,0},
				{0,0,0,0,0,0,0,1,-1}
				};
	
		/*
		 * Solver类有一个静态函数 solve. 
		 * 第一个参数是关联矩阵。第二个参数指明计算T还是P不变量，
		 * 第二个参数如果不指定是计算T不变量，如果指定可以是字符串"T"或"P"或者bool变量，true代表计算T变量false代表计算P不变量
		 * 返回是1和0的数组，数组长度等于变量的数量，跟本档中计算的不变量含义一致。
		 */
		
		ArrayList<int[]> ans = Solver.solve(a); 
		// ans = Solver.solve(a, "T");  ans = Solver.solve(a, true); 都可以
		System.out.println("T不变量：");
		for(int i=0;i<ans.size();i++) {
			int[] _t = ans.get(i);
			for(int j=0;j<_t.length;j++){
				System.out.print(_t[j]);
				System.out.print(',');
			}
			System.out.println("\n");
		}
		
		ans = Solver.solve(a, "P"); 
		//ans = Solver.solve(a, false); 也可以
		System.out.println("P不变量：");
		for(int i=0;i<ans.size();i++) {
			int[] _t = ans.get(i);
			for(int j=0;j<_t.length;j++){
				System.out.print(_t[j]);
				System.out.print(',');
			}
			System.out.println("\n");
		}
	}
}
