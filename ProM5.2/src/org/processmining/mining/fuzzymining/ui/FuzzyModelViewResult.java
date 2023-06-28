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
package org.processmining.mining.fuzzymining.ui;

import javax.swing.JComponent;

import org.processmining.framework.log.LogReader;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.framework.plugin.Provider;
import org.processmining.mining.MiningResult;
import org.processmining.mining.fuzzymining.edit.FuzzyGraphEditor;
import org.processmining.mining.fuzzymining.graph.MutableFuzzyGraph;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 * 
 */
public class FuzzyModelViewResult implements MiningResult, Provider {

	protected FuzzyGraphEditor editor;
	protected FuzzyModelPanel modelPanel;

	public FuzzyModelViewResult(MutableFuzzyGraph graph) {
		this.editor = new FuzzyGraphEditor(graph);
		this.modelPanel = new FuzzyModelPanel(graph, null, null, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.staticgenerator.mining.MiningResult#getLogReader()
	 */
	public LogReader getLogReader() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.staticgenerator.mining.MiningResult#getVisualization()
	 */
	public JComponent getVisualization() {
		// return editor;
		return modelPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.staticgenerator.framework.plugin.Provider#getProvidedObjects()
	 */
	public ProvidedObject[] getProvidedObjects() {
		return editor.getProvidedObjects();
	}

}
