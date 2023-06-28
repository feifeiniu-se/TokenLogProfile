package cn.edu.nju.loggenerate.staticgenerator.tInvariant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;

/**
 * 提取特征库所 
 * 所谓特征库所就是or-split和or-join的库所
 * @author Administrator
 *
 */
public class ExtractFeaturePlace {

	/**
	 * 从一个流程模型中提取特征库所
	 * @param processModel
	 * @return
	 */
	public Set<Place> extract(ProcessModel processModel){
		Set<Place> featurePlaces = new HashSet<Place>();
		for( Place place : processModel.getPlaces().getPlaces() ){
			if( isFeaturePlace(place) ){
				featurePlaces.add(place);
			}
		}
		return featurePlaces;
	}
	
	/**
	 * 判断是否为特征库所
	 * 库所的前驱变迁或后继变迁大于1
	 * @param place
	 * @return
	 */
	private boolean isFeaturePlace(Place place){
		List<String> inputs = place.getInputs();
		List<String> outputs = place.getOutputs();
		boolean flag = false;
		if( null != inputs && inputs.size() > 1 ){
			flag = true;
		}else if( null != outputs && outputs.size() > 1 ){
			flag = true;
		}
		return flag;
	}
	
}
