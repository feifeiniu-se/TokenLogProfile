package cn.edu.nju.loggenerate.staticgenerator.tInvariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;

/**
 * 把循环结构的T不变量附加到LMST不变量上
 * @author Administrator
 *
 */
public class InvariantAppend {

    private ProcessModel lmstModel;
    private List<ProcessModel> loopModels;

    public InvariantAppend(ProcessModel lmstModel, List<ProcessModel> loopModels) {
        this.lmstModel = lmstModel;
        this.loopModels = new ArrayList<ProcessModel>();
        for (ProcessModel pm : loopModels) {
            if(!pm.isLoopModelAdded()) {
                this.loopModels.add(pm);
            }
        }
        // this.lmstModel = lmstModel;
        // this.loopModels = loopModels;
    }

    public LoopRules loopRulesGen(Map<ProcessModel, Set<Place>> modelFeaturePlaceMap) {

        boolean flag = true;
        int loopLevel = 0;

        LoopRules root = new LoopRules();
        root.setOriModel(lmstModel);
        List<LoopRules> rulesList = new ArrayList<LoopRules>();
        rulesList.add(root);

        while (flag) {
            flag = false;// 如果下面没有loop附加上去，while循环结束
            List<ProcessModel> waitAppendLoopModel = new ArrayList<ProcessModel>();
            List<LoopRules> newRulesList = new ArrayList<LoopRules>();

            for (LoopRules loopRules : rulesList) {
                ProcessModel baseAppendModel = loopRules.getOriModel();
                Set<Place> baseAppendFeaturePlaces = modelFeaturePlaceMap.get(baseAppendModel);

                for (ProcessModel loopModel : loopModels) {
                    List<Place> commonFeaturePlaces = new ArrayList<Place>();

                    Set<Place> loopFeaturePlaces = modelFeaturePlaceMap.get(loopModel);
                    int count = 0;
                    // 统计lmst和loop之间重叠的特征库所
                    for (Place featurePlace : loopFeaturePlaces) {
                        if (baseAppendFeaturePlaces.contains(featurePlace)) {
                            count++;
                            commonFeaturePlaces.add(featurePlace);
                        }
                    }

                    if(count > 0) {
                        waitAppendLoopModel.add(loopModel);
                        loopModel.setLoopModelAdded(true);
                        LoopRules newRules = this.buildLooRules(baseAppendModel, loopModel,
                                commonFeaturePlaces, loopLevel);
                        loopRules.addSubRules(newRules);
                        newRulesList.add(newRules);
                        flag = true;// 设置下个while可以执行
                    }
                } // end for

            } // end for

            // 去除已经附加过的loopModel
            for (ProcessModel appendLoopModel : waitAppendLoopModel) {
                loopModels.remove(appendLoopModel);
            }
            if (loopModels.size() == 0) {
                break;
            }
            rulesList = newRulesList;
            loopLevel++;
        } // end while
        return root;
    }


    private LoopRules buildLooRules(ProcessModel baseAppendModel, ProcessModel loopModel,
                                    List<Place> commonFeaturePlaces, int loopLevel) {
        Place singlePlace = commonFeaturePlaces.get(0);
        String insertAfterTransition = "";
        String insertBeforeTransition = "";

        for (String input : singlePlace.getInputs()) {
            if (baseAppendModel.getTransitionByName(input) != null) {
                insertAfterTransition = input;
                break;
            }
        }
        for (String output : singlePlace.getOutputs()) {
            if (baseAppendModel.getTransitionByName(output) != null) {
                insertBeforeTransition = output;
                break;
            }
        }
        String[] insertBetween = { insertAfterTransition, insertBeforeTransition };

        String startPlaceName = singlePlace.getName();
        ProcessModel newLoopModel = loopModel.copyModel();
        Place sp = newLoopModel.getPlaceByName(startPlaceName);
        Place spStart = new Place(startPlaceName + "_start");
        Place spEnd = new Place(startPlaceName + "_end");

        for (String it : sp.getInputs()) {
            Transition t = newLoopModel.getTransitionByName(it);
            if (t != null) {
                t.getOutputs().remove(startPlaceName);
                t.getOutputs().add(spEnd.getName());
                spEnd.getInputs().add(t.getName());
            }
        }

        for (String ot : sp.getOutputs()) {
            Transition t = newLoopModel.getTransitionByName(ot);
            if (t != null) {
                t.getInputs().remove(startPlaceName);
                t.getInputs().add(spStart.getName());
                spStart.getOutputs().add(t.getName());
            }
        }

        newLoopModel.getPlaces().getPlaces().remove(sp);
        newLoopModel.getPlaces().getPlaces().add(spEnd);
        newLoopModel.getPlaces().getPlaces().add(spStart);
        newLoopModel.setStartPlace(spStart);
        newLoopModel.setEndPlace(spEnd);

        return new LoopRules(loopModel, newLoopModel, singlePlace.getName(), insertBetween, loopLevel);
    }


//	private LoopRules buildLooRulesWithSinglePlaceEvo(ProcessModel baseAppendModel, ProcessModel loopModel,
//			List<Place> commonFeaturePlaces, int loopLevel) {
//
//		Place singlePlace = commonFeaturePlaces.get(0);
//
////		List<String> commonTransition = new ArrayList<String>();
////		List<String> distinctTransition = new ArrayList<String>();
////		for (Transition transitionLoop : loopModel.getTransitions().getTransitions()) {
////			if (baseAppendModel.getTransitionByName(transitionLoop.getName()) != null) {
////				commonTransition.add(transitionLoop.getName());
////			} else {
////				distinctTransition.add(transitionLoop.getName());
////			}
////		}
////
////		if (commonTransition.size() == 0) {
////			singlePlace = commonFeaturePlaces.get(0);
////		} else {
////			for (Place place : loopModel.getPlaces().getPlaces()) {
////				List<String> inputs = place.getInputs();
////				List<String> outputs = place.getOutputs();
////				if (inputHasCommonButNoDistinct(inputs, commonTransition, distinctTransition)
////						&& outputHasDistinctButNoCommon(outputs, commonTransition, distinctTransition)) {
////					singlePlace = place;
////					break;
////				}
////			}
////		}
//
//		String insertAfterTransition = "";
//		String insertBeforeTransition = "";
//
//		for (String input : singlePlace.getInputs()) {
//			if (baseAppendModel.getTransitionByName(input) != null) {
//				insertAfterTransition = input;
//				break;
//			}
//		}
//		for (String output : singlePlace.getOutputs()) {
//			if (baseAppendModel.getTransitionByName(output) != null) {
//				insertBeforeTransition = output;
//				break;
//			}
//		}
//		String[] insertBetween = { insertAfterTransition, insertBeforeTransition };
//
//		String startPlaceName = singlePlace.getName();
//		ProcessModel newLoopModel = loopModel.copyModel();
//		Place sp = newLoopModel.getPlaceByName(startPlaceName);
//		Place spStart = new Place(startPlaceName + "_start");
//		Place spEnd = new Place(startPlaceName + "_end");
//
//		for (String it : sp.getInputs()) {
//			Transition t = newLoopModel.getTransitionByName(it);
//			if (t != null) {
//				t.getOutputs().remove(startPlaceName);
//				t.getOutputs().add(spEnd.getName());
//				spEnd.getInputs().add(t.getName());
//			}
//		}
//
//		for (String ot : sp.getOutputs()) {
//			Transition t = newLoopModel.getTransitionByName(ot);
//			if (t != null) {
//				t.getInputs().remove(startPlaceName);
//				t.getInputs().add(spStart.getName());
//				spStart.getOutputs().add(t.getName());
//			}
//		}
//
//		newLoopModel.getPlaces().getPlaces().remove(sp);
//		newLoopModel.getPlaces().getPlaces().add(spEnd);
//		newLoopModel.getPlaces().getPlaces().add(spStart);
//		newLoopModel.setStartPlace(spStart);
//		newLoopModel.setEndPlace(spEnd);
//
//		return new LoopRules(loopModel, newLoopModel, singlePlace.getName(), insertBetween, loopLevel);
//	}
//
//	private LoopRules buildLoopRulesEvo(ProcessModel baseAppendModel, ProcessModel loopModel,
//			List<Place> commonFeaturePlaces, int loopLevel) {
///*-------------------------------------------------找到循环的开始库所！！！--------------------------------------------------------*/
//		//在共有的里面随意找一个就行了
////		List<String> commonTransition = new ArrayList<String>();
////		List<String> distinctTransition = new ArrayList<String>();
////		for (Transition transitionLoop : loopModel.getTransitions().getTransitions()) {
////			if (baseAppendModel.getTransitionByName(transitionLoop.getName()) != null) {
////				commonTransition.add(transitionLoop.getName());
////			} else {
////				distinctTransition.add(transitionLoop.getName());
////			}
////		}
////
////		// 找出起始和结束库所
////		Place endPlace = null;
////		if(commonTransition.size() != 0) {
////		for (Place commonPlace : commonFeaturePlaces) {
////			List<String> inputs = commonPlace.getInputs();
////			List<String> outputs = commonPlace.getOutputs();
////			if (inputHasCommonButNoDistinct(inputs, commonTransition, distinctTransition)
////					&& outputHasDistinctButNoCommon(outputs, commonTransition, distinctTransition)) {
////				endPlace = commonPlace;
////				break;
////			}
////		}
////		}else {
////			for (Place commonPlace : commonFeaturePlaces) {
////				List<String> inputs = commonPlace.getInputs();
////				List<String> outputs = commonPlace.getOutputs();
////
////			}
////		}
//
//		Place singlePlace = commonFeaturePlaces.get(0);
//		String startPlaceName = singlePlace.getName();
//
//		ProcessModel newLoopModel = loopModel.copyModel();
//		Place sp = newLoopModel.getPlaceByName(startPlaceName);
//		Place spStart = new Place(startPlaceName + "_start");
//		Place spEnd = new Place(startPlaceName + "_end");
//
//		for (String it : sp.getInputs()) {
//			Transition t = newLoopModel.getTransitionByName(it);
//			if (t != null) {
//				t.getOutputs().remove(startPlaceName);
//				t.getOutputs().add(spEnd.getName());
//				spEnd.getInputs().add(t.getName());
//			}
//		}
//
//		for (String ot : sp.getOutputs()) {
//			Transition t = newLoopModel.getTransitionByName(ot);
//			if (t != null) {
//				t.getInputs().remove(startPlaceName);
//				t.getInputs().add(spStart.getName());
//				spStart.getOutputs().add(t.getName());
//			}
//		}
//
//		newLoopModel.getPlaces().getPlaces().remove(sp);
//		newLoopModel.getPlaces().getPlaces().add(spEnd);
//		newLoopModel.getPlaces().getPlaces().add(spStart);
//		newLoopModel.setStartPlace(spStart);
//		newLoopModel.setEndPlace(spEnd);
//
//		// 根据endPlace，找出插入的起始和结束活动
//		String insertAfterTransition = "";
//		String insertBeforeTransition = "";
//
//		for (String input : singlePlace.getInputs()) {
//			if (baseAppendModel.getTransitionByName(input) != null) {
//				insertAfterTransition = input;
//				break;
//			}
//		}
//
//		for (String output : singlePlace.getOutputs()) {
//			if (lmstModel.getTransitionByName(output) != null) {
//				insertBeforeTransition = output;
//				break;
//			}
//		}
//		String[] insertBetween = { insertAfterTransition, insertBeforeTransition };
//
//		return new LoopRules(loopModel, newLoopModel, insertBetween, loopLevel);
//	}
//
//	private boolean outputHasDistinctButNoCommon(List<String> outputs, List<String> commonTransition,
//			List<String> distinctTransition) {
//		// TODO Auto-generated method stub
//
//		return inputHasCommonButNoDistinct(outputs, distinctTransition, commonTransition);
//	}
//
//	private boolean inputHasCommonButNoDistinct(List<String> inputs, List<String> commonTransition,
//			List<String> distinctTransition) {
//		// TODO Auto-generated method stub
//		boolean hasCommon = false;
//		for (String input : inputs) {
//			if (commonTransition.contains(input)) {
//				hasCommon = true;
//				break;
//			}
//		}
//		if (hasCommon) {
//			for (String input : inputs) {
//				if (distinctTransition.contains(input)) {
//					return false;
//				}
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}

//	private Map<ProcessModel, Integer> generateAppendMark(List<ProcessModel> loopModels) {
//		Map<ProcessModel, Integer> mark = new HashMap<ProcessModel, Integer>();
//		for (ProcessModel loopModel : loopModels) {
//			mark.put(loopModel, 0);
//
//		}
//		return mark;
//	}
//
//	public void append(ProcessModel lmstModel, ProcessModel loopModel, Set<Place> lmstFeaturePlaces,
//			Set<Place> loopFeaturePlaces) {
//		List<Transition> transitions = loopModel.getTransitions().getTransitions();
//		List<Place> places = loopModel.getPlaces().getPlaces();
//		for (Transition transition : transitions) {
//			lmstModel.getTransitions().addTransition(transition);
//		}
//		for (Place place : places) {
//			lmstModel.getPlaces().addPlace(place);
//		}
//		// 把loopModel的特征库所附加到lmstModel的特征库所上
//		for (Place featurePlace : loopFeaturePlaces) {
//			lmstFeaturePlaces.add(featurePlace);
//		}
//	}


//	/**
//	 * 附加规则 如果loopmodel就仅有特征库所，和lmstmodel有重合，加上
//	 * loopmodel中的特征库所如果在lmstmodel的特征库所重合2个以上，加上
//	 */
//	public void appendModel(Map<ProcessModel, Set<Place>> modelFeaturePlaceMap, List<ProcessModel> lmstModels) {
//		for (ProcessModel lmstModel : lmstModels) {
//			Set<Place> lmstFeaturePlaces = modelFeaturePlaceMap.get(lmstModel);
//			List<LoopRules> rulesList = new ArrayList<LoopRules>();
//
//			// 每个lmst重新生成个mark
//			Map<ProcessModel, Integer> mark = this.generateAppendMark(loopModels);
//			boolean flag = true;
//			int loopLevel = 0;
//
//			while (flag) {
//				flag = false;// 如果下面没有loop附加上去，while循环结束
//				List<ProcessModel> waitAppendLoopModel = new ArrayList<ProcessModel>();
//				for (ProcessModel loopModel : loopModels) {
//					if (mark.get(loopModel) == 1)
//						continue;// 已经添加过的话，就跳过
//					Set<Place> loopFeaturePlaces = modelFeaturePlaceMap.get(loopModel);
//					List<Place> commonFeaturePlaces = new ArrayList<Place>();
//
//					int count = 0;
//					// 统计lmst和loop之间重叠的特征库所
//					for (Place featurePlace : loopFeaturePlaces) {
//						if (lmstFeaturePlaces.contains(featurePlace)) {
//							count++;
//							commonFeaturePlaces.add(featurePlace);
//						}
//					}
//					if ((1 == count && loopFeaturePlaces.size() == 1) || count >= 2) {
//						// append(lmstModel, loopModel, lmstFeaturePlaces, loopFeaturePlaces);//进行附加
//						// 变迁，库所，特征库所
//						// 之前是上面的代码，即刻append的，但是下次在这个循环中，会append 更上级的looplevel，所以要在一次循环后再append
//						waitAppendLoopModel.add(loopModel);
//
//						if (1 == count) {
//							rulesList.add(this.buildLooRulesWithSinglePlace(lmstModel, loopModel, commonFeaturePlaces,
//									loopLevel));
//						} else {
//							rulesList.add(this.buildLoopRules(lmstModel, loopModel, commonFeaturePlaces, loopLevel));
//						}
//						mark.put(loopModel, 1);// 记录已经附加过了
//						flag = true;// 设置下个while可以执行
//					}
//				} // end for
//					// 进行append loop
//				for (ProcessModel appendLoopModel : waitAppendLoopModel) {
//					Set<Place> loopFeaturePlaces = modelFeaturePlaceMap.get(appendLoopModel);
//					this.append(lmstModel, appendLoopModel, lmstFeaturePlaces, loopFeaturePlaces);
//				}
//				loopLevel++;
//			} // end while
//		}
//	}
//
//	// 如果只有一个特征库所
//	private LoopRules buildLooRulesWithSinglePlace(ProcessModel baseAppendModel, ProcessModel loopModel,
//			List<Place> commonFeaturePlaces, int loopLevel) {
//		Place singlePlace = commonFeaturePlaces.get(0);
//		String insertAfterTransition = "";
//		String insertBeforeTransition = "";
//
//		for (String input : singlePlace.getInputs()) {
//			if (baseAppendModel.getTransitionByName(input) != null) {
//				insertAfterTransition = input;
//				break;
//			}
//		}
//		for (String output : singlePlace.getOutputs()) {
//			if (baseAppendModel.getTransitionByName(output) != null) {
//				insertBeforeTransition = output;
//				break;
//			}
//		}
//		String[] insertBetween = { insertAfterTransition, insertBeforeTransition };
//
//		return new LoopRules(loopModel, singlePlace.getName(), insertBetween, loopLevel);
//	}

//	private LoopRules buildLoopRules(ProcessModel baseAppendModel, ProcessModel loopModel,
//			List<Place> commonFeaturePlaces, int loopLevel) {
//		List<Transition> loopTransitions = loopModel.getTransitions().getTransitions();
//
//		TransitionList partloopTransitions = new TransitionList();
//		PlaceList partloopPlaces = new PlaceList();
//		TransitionList mainTransitions = new TransitionList();
//		PlaceList mainPlaces = new PlaceList();
//
//		// 区分开两个model 的变迁
//		for (Transition t : loopTransitions) {
//			if (baseAppendModel.getTransitions().getTransitions().contains(t)) {
//				mainTransitions.addTransition(t);
//			} else {
//				partloopTransitions.addTransition(t);
//			}
//		}
//		// 添加loop model的place
//		for (Transition t : partloopTransitions.getTransitions()) {
//			List<String> inputs = t.getInputs();
//			List<String> outputs = t.getOutputs();
//			for (String input : inputs) {
//				partloopPlaces.addPlace(loopModel.getPlaceByName(input));
//			}
//			for (String output : outputs) {
//				partloopPlaces.addPlace(loopModel.getPlaceByName(output));
//			}
//		}
//		ProcessModel partloopModel = new ProcessModel(partloopPlaces, partloopTransitions);
//
//		// 添加main model的place
//		for (Transition t : mainTransitions.getTransitions()) {
//			List<String> inputs = t.getInputs();
//			List<String> outputs = t.getOutputs();
//			for (String input : inputs) {
//				mainPlaces.addPlace(baseAppendModel.getPlaceByName(input));
//			}
//			for (String output : outputs) {
//				mainPlaces.addPlace(baseAppendModel.getPlaceByName(output));
//			}
//		}
//		ProcessModel mainModel = new ProcessModel(mainPlaces, mainTransitions);
//
//		// 找出起始和结束库所
//		Place startPlace = null;
//		Place endPlace = null;
//		for (Place commonPlace : commonFeaturePlaces) {
//			List<String> inputs = commonPlace.getInputs();
//			boolean isStartPlace = false;
//			for (String input : inputs) {
//				if (loopModel.getTransitionByName(input) == null) {
//					isStartPlace = true;
//					startPlace = commonPlace;
//					break;
//				}
//			}
//			if (!isStartPlace) {
//				endPlace = commonPlace;
//
//			}
//		}
//
//		partloopModel.setStartPlace(endPlace);
//		partloopModel.setEndPlace(startPlace);
//
//		mainModel.setStartPlace(startPlace);
//		mainModel.setEndPlace(endPlace);
//
//		// 根据endPlace，找出插入的起始和结束活动
//		String insertAfterTransition = "";
//		String insertBeforeTransition = "";
//
//		for (String input : endPlace.getInputs()) {
//			if (mainTransitions.getTransitionByName(input) != null) {
//				insertAfterTransition = input;
//				break;
//			}
//		}
//
//		for (String output : endPlace.getOutputs()) {
//			if (lmstModel.getTransitionByName(output) != null) {
//				insertBeforeTransition = output;
//				break;
//			}
//		}
//		String[] insertBetween = { insertAfterTransition, insertBeforeTransition };
//
//		return new LoopRules(loopModel, partloopModel, mainModel, insertBetween, loopLevel);
//	}

}
