package cn.edu.nju.loggenerate.staticgenerator.math;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;

public class GetTransitionArrays {

	public static List<List<List<String>>> solve(ProcessModel pm) {
		List<List<List<String>>> rtn = new ArrayList<List<List<String>>>();

		int[][] totalMatrix = CalculateMatrix.calcMatrix(pm);
		//PrintMatrix.printRelationMatrix(totalMatrix, pm);

		ArrayList<int[]> LMS_T_out = Solver.solve(totalMatrix, "T");

		List<ProcessModel> new_pms = CalculateMatrix.getModelsByIntArray(
				LMS_T_out, pm);
		for (ProcessModel new_pm : new_pms) {
			int[][] pMatrixTmp = CalculateMatrix.calcMatrix(new_pm);

			//PrintMatrix.printRelationMatrix(pMatrixTmp, new_pm);

			ArrayList<int[]> LMS_P_out_Tmp = Solver.solve(pMatrixTmp, "P");

			List<List<String>> tNames_list = CalculateMatrix
					.getTasksByIntArray(LMS_P_out_Tmp, new_pm, pm);

			rtn.add(tNames_list);
		}

		//PrintMatrix.printListListList(rtn);
		return rtn;
	}

	public static int[][] getPInputArray(ProcessModel pm) {
		int[][] totalMatrix = CalculateMatrix.calcMatrix(pm);
		//PrintMatrix.printRelationMatrix(totalMatrix, pm);

		ArrayList<int[]> LMS_T_out = Solver.solve(totalMatrix, "T");

		List<ProcessModel> new_pms = CalculateMatrix.getModelsByIntArray(
				LMS_T_out, pm);
		for (ProcessModel new_pm : new_pms) {
			int[][] pMatrixTmp = CalculateMatrix.calcMatrix(new_pm);
			return pMatrixTmp;
		}
		return null;
	}
}
