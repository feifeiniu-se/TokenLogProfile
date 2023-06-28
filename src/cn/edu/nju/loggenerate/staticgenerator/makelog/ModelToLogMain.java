package cn.edu.nju.loggenerate.staticgenerator.makelog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.nju.loggenerate.staticgenerator.concurrentRegion.ConcurrentAndLoopStatistics;
import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.tInvariant.ExtractFeaturePlace;
import cn.edu.nju.loggenerate.staticgenerator.tInvariant.InvariantAppend;
import cn.edu.nju.loggenerate.staticgenerator.tInvariant.LoopRules;
import cn.edu.nju.loggenerate.staticgenerator.tInvariant.ModelConverter;

import cn.edu.nju.loggenerate.staticgenerator.tInvariant.TraceGen2;

public class ModelToLogMain {

//	public static void main(String[] args) {
//		CreateModel cm = new CreateModel();
//		ProcessModel pm = cm
//				.getProcessModel(
//						"C:\\Users\\nju\\workspace\\GetProcessLog\\src\\files\\loop1\\places.txt",
//						"C:\\Users\\nju\\workspace\\GetProcessLog\\src\\files\\loop1\\transitions.txt");
//	
//		// System.out.println(pm.toString());
//	
//		// 得到T，P分解后的Transition list
//		List<List<List<String>>> t_pathes = GetTransitionArrays.solve(pm);
//		FileQueue outFile = new FileQueue("log.txt", "E:\\");
//		MakeLog.setPath("E:/");
//		ModelConverter converter = new ModelConverter(pm);
//		List<List<String>> results = TraceGen2.TinvariantResult(pm);
//		int mm = 0;
//
//		List<ProcessModel> lmstModels = converter.convertModelsFromResult(
//				results, "-", true);
//		// 转化为非LMSTmodel
//		List<ProcessModel> loopModels = converter.convertModelsFromResult(
//				results, "-", false);
//
//		for (List<List<String>> path : t_pathes) {
//			PInvariantToModel ptm = new PInvariantToModel(pm);
//			// 根据T，P分解后的Transition List得到对应的ProcessModel
//			if (TraceGen2.check(path)) {
//				List<ProcessModel> pms = ptm.convertToModel(path);
//
//				ConcurrentRegion cr = new ConcurrentRegion(pm);
//				cr.genConcurrentResions(pms);
//				List<String> mainPathNames = cr.getMainPathTNames();
//				List<RegionAndRules> regionAndRulesList = cr
//						.getRegionAndRulesList();
//
//				FileQueue outQueue = MakeLog.dealWithAllParts(
//						regionAndRulesList, mainPathNames);
//
//				List<String> q_out;
//				int number = 0;
//				while ((q_out = outQueue.pollList()) != null) {
//					outFile.offer(q_out, number);
//					number++;
//				}
//
//				outQueue.close();
//			}
//		}
//		// 提取特征库所
//		ExtractFeaturePlace efp = new ExtractFeaturePlace();
//		Set<Place> featurePlaces = efp.extract(pm);
//		System.out.println("output featureplaces");
//		for (Place place : featurePlaces) {
//			System.out.println(place.getName());
//
//		}
//
//		// 转化为LMSTmodel
//
//		// 生成modelFeaturePlaceModel
//		Map<ProcessModel, Set<Place>> modelFeaturePlaceMap = new HashMap<ProcessModel, Set<Place>>();
//		for (ProcessModel model : lmstModels) {
//			modelFeaturePlaceMap.put(model, efp.extract(model));
//		}
//		for (ProcessModel model : loopModels) {
//			modelFeaturePlaceMap.put(model, efp.extract(model));
//		}
//
//		String filePath = "C:\\Users\\nju\\workspace\\GetProcessLog";
//		System.out.println("lmstModels.size()" + lmstModels.size());
//
//		for (ProcessModel lmstModel : lmstModels) {
//			lmstModel.setStartPlace(pm.getStartPlace());
//			lmstModel.setEndPlace(pm.getEndPlace());
//			InvariantAppend invariantAppend = new InvariantAppend(lmstModel,
//					loopModels);
//			LoopRules root = invariantAppend.loopRulesGen(modelFeaturePlaceMap);
//
//			/*
//			 * ProcessModel test=root.getSubRules().get(1).getPartloopModel();
//			 * getProcessModel(test); computer(test,"main.txt");
//			 */
//			TraceGen2.rootVar(root, mm + "final.txt", filePath);
//			System.out.println(root);
//			mm++;
//		}
//		outFile.closeWithoutDelete();
//	}

//	public static void makeLog(ProcessModel pm, String logPath) {
//
//		List<List<List<String>>> t_pathes = GetTransitionArrays.solve(pm);
//
//		FileQueue outFile = new FileQueue(logPath);
//
//		for (List<List<String>> path : t_pathes) {
//			PInvariantToModel ptm = new PInvariantToModel(pm);
//			List<ProcessModel> pms = ptm.convertToModel(path);
//
//			ConcurrentRegion cr = new ConcurrentRegion(pm);
//			cr.genConcurrentResions(pms);
//			List<String> mainPathNames = cr.getMainPathTNames();
//			List<RegionAndRules> regionAndRulesList = cr
//					.getRegionAndRulesList();
//
//			FileQueue outQueue = MakeLog.dealWithAllParts(regionAndRulesList,
//					mainPathNames);
//
//			List<String> q_out;
//			int number = 0;
//			while ((q_out = outQueue.pollList()) != null) {
//				outFile.offer(q_out, number);
//				number++;
//			}
//
//			outQueue.close();
//		}
//
//		outFile.closeWithoutDelete();
//	}

	public static List<List<String>> makeLog(ProcessModel pm) {
		List<List<String>> allTraces = new ArrayList<List<String>>();
		
		ModelConverter converter = new ModelConverter(pm);
		List<List<String>> results = TraceGen2.TinvariantResult(pm);
		int mm = 0;

		List<ProcessModel> lmstModels = converter.convertModelsFromResult(
				results, "-", true);
		// 转化为非LMSTmodel
		List<ProcessModel> loopModels = converter.convertModelsFromResult(
				results, "-", false);
		ConcurrentAndLoopStatistics.loops = loopModels.size();

		// 提取特征库所
		ExtractFeaturePlace efp = new ExtractFeaturePlace();

		Map<ProcessModel, Set<Place>> modelFeaturePlaceMap = new HashMap<ProcessModel, Set<Place>>();
		for (ProcessModel model : lmstModels) {
			modelFeaturePlaceMap.put(model, efp.extract(model));
		}
		for (ProcessModel model : loopModels) {
			modelFeaturePlaceMap.put(model, efp.extract(model));
		}

		for (ProcessModel lmstModel : lmstModels) {
			ConcurrentAndLoopStatistics.initialize();
			lmstModel.setStartPlace(pm.getStartPlace());
			lmstModel.setEndPlace(pm.getEndPlace());
			InvariantAppend invariantAppend = new InvariantAppend(lmstModel,
					loopModels);
			LoopRules root = invariantAppend.loopRulesGen(modelFeaturePlaceMap);

			allTraces.addAll(TraceGen2.rootVar(root).closeWithoutDelete());
			mm++;

			ConcurrentAndLoopStatistics.finalTraces = ConcurrentAndLoopStatistics.finalTraces + ConcurrentAndLoopStatistics.numberOfTraces;
		}
		return allTraces;
	}
}
