/*
 * Copyright (c) 2007 Christian W. Guenther (christian@deckfour.org)
 * 
 * LICENSE:
 * 
 * This code is free loggenerate; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * 
 * EXEMPTION:
 * 
 * License to link and use is also granted to open source programs which
 * are not licensed under the terms of the GPL, given that they satisfy one
 * or more of the following conditions:
 * 1) Explicit license is granted to the ProM and ProMimport programs for
 *    usage, linking, and derivative work.
 * 2) Carte blance license is granted to all programs developed at
 *    Eindhoven Technical University, The Netherlands, or under the
 *    umbrella of STW Technology Foundation, The Netherlands.
 * For further exemptions not covered by the above conditions, please
 * contact the author of this code.
 * 
 */
package org.processmining.analysis.traceclustering.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.processmining.analysis.petrinet.cpnexport.CpnUtils;
import org.processmining.analysis.traceclustering.distance.DistanceMatrix;
import org.processmining.analysis.traceclustering.distance.DistanceMetric;
import org.processmining.analysis.traceclustering.distance.DoubleDistanceMatrix;
import org.processmining.analysis.traceclustering.distance.FloatDistanceMatrix;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.ui.Message;
import org.processmining.framework.ui.Progress;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 * 
 */
public class AggregateProfile extends Profile {

	protected Set<Profile> profiles;
	protected List<String> itemList;
	protected Map<String, Profile> itemProfileMap;
	protected Map<String, String> itemItemMap;
	protected DistanceMatrix distanceMatrix;

	/**
	 * @param name
	 * @param description
	 */
	public AggregateProfile(LogReader aLog) {
		super(aLog, "Aggregate profile", "An aggregation of multiple profiles");
		profiles = new HashSet<Profile>();
		itemList = new ArrayList<String>();
		itemProfileMap = new HashMap<String, Profile>();
		itemItemMap = new HashMap<String, String>();
		normalizationMaximum = 1.0;
		invert = false;
		distanceMatrix = null;
	}

	public void addProfile(Profile aProfile) {
		profiles.add(aProfile);
		String profileName = aProfile.getName();
		for (String origItem : aProfile.getItemKeys()) {
			String item = profileName + "." + origItem;
			itemList.add(item);
			itemProfileMap.put(item, aProfile);
			itemItemMap.put(item, origItem);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.staticgenerator.analysis.traceclustering.profile.Profile#getDistanceMatrix
	 * (org.staticgenerator.analysis.traceclustering.distance.DistanceMetric)
	 */
	@Override
	public DistanceMatrix getDistanceMatrix(DistanceMetric metric,
			Progress progress) {
		if (distanceMatrix == null) {
			progress.setMinMax(0, profiles.size() + 1);
			int progressCounter = 1;
			// initialize data structures
			int numberOfInstances = log.numberOfInstances();
			if (numberOfInstances < 500) {
				distanceMatrix = new DoubleDistanceMatrix(numberOfInstances);
			} else {
				distanceMatrix = new FloatDistanceMatrix(numberOfInstances);
			}
			double maximumValue = 0.0;
			DistanceMatrix tmpMatrix;
			// iterate over aggregated profiles
			for (Profile profile : profiles) {
				// handle progress feedback
				progress.setProgress(progressCounter);
				progress.setNote("Measuring " + profile.getName() + " using "
						+ metric.getName() + " metric...");
				progressCounter++;
				// aggregate distance matrices (add each cell up)
				maximumValue += profile.getNormalizationMaximum();
				tmpMatrix = profile.getDistanceMatrix(metric, progress);
				distanceMatrix.add(tmpMatrix);
			}
			// normalize distance matrix to 1.0 (max.)
			distanceMatrix.normalizeToMaximum(1.0);
		}
		return distanceMatrix;
	}

	public void resetDistanceMatrix() {
		distanceMatrix = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.staticgenerator.analysis.traceclustering.profile.Profile#getItemKey
	 * (int)
	 */
	@Override
	public String getItemKey(int index) {
		return itemList.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.staticgenerator.analysis.traceclustering.profile.Profile#getItemKeys()
	 */
	@Override
	public List<String> getItemKeys() {
		return itemList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.staticgenerator.analysis.traceclustering.profile.Profile#getValue(int,
	 * int)
	 */
	@Override
	public double getValue(int instanceIndex, int itemIndex) {
		String item = itemList.get(itemIndex);
		Profile profile = itemProfileMap.get(item);
		String origItem = itemItemMap.get(item);
		double normalizationFactor = normalizationMaximum
				/ profile.getNormalizationMaximum();
		double value = profile.getValue(instanceIndex, origItem)
				* normalizationFactor;
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.staticgenerator.analysis.traceclustering.profile.Profile#getValue(int,
	 * java.lang.String)
	 */
	@Override
	public double getValue(int instanceIndex, String itemId) {
		Profile profile = itemProfileMap.get(itemId);
		String origItem = itemItemMap.get(itemId);
		double normalizationFactor = normalizationMaximum
				/ profile.getNormalizationMaximum();
		double value = profile.getValue(instanceIndex, origItem)
				* normalizationFactor;
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.staticgenerator.analysis.traceclustering.profile.Profile#numberOfItems
	 * ()
	 */
	@Override
	public int numberOfItems() {
		return itemList.size();
	}

	@Override
	public double getRandomValue(int itemIndex) {
		String item = itemList.get(itemIndex);
		Profile profile = itemProfileMap.get(item);
		String origItem = itemItemMap.get(item);
		return profile.getRandomValue(origItem);
	}

	@Override
	public double getRandomValue(String itemId) {
		Profile profile = itemProfileMap.get(itemId);
		String origItem = itemItemMap.get(itemId);
		return profile.getRandomValue(origItem);
	}

	public Instances getWekaData() {
		Instances data = null;

		// create attribute information
		FastVector attributeInfo = new FastVector();
		// make attribute
		// clean the relevant attribute list and re-fill based on new selection
		// scope
		for (int i = 0; i < numberOfItems(); i++) {
			String name = CpnUtils.replaceSpecialCharacters(getItemKey(i));
			Attribute wekaAtt = new Attribute(name);
			attributeInfo.addElement(wekaAtt);
		}
		attributeInfo.trimToSize();
		data = new Instances("Clustering", attributeInfo, 0);
		try {
			for (int i = 0; i < getLog().numberOfInstances(); i++) {
				Instance instance0 = new Instance(attributeInfo.size());
				for (int j = 0; j < numberOfItems(); j++) {
					String name = CpnUtils
							.replaceSpecialCharacters(getItemKey(j));
					Attribute wekaAtt = data.attribute(name);
					if (wekaAtt != null) {
						double doubleAttValue = (new Double(getValue(i, j)))
								.doubleValue();
						instance0.setValue(wekaAtt, doubleAttValue);
					} else {
						Message.add("Weka Error: fail to add", Message.ERROR);
					}
				}
				instance0.setDataset(data);
				data.add(instance0);
			}
		} catch (Exception c) {
			Message.add("Weka Error: " + c.toString(), Message.ERROR);
		}

		return data;
	}

}
