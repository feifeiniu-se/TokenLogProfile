/***********************************************************
 *      This loggenerate is part of the ProM package          *
 *             http://www.processmining.org/               *
 *                                                         *
 *            Copyright (c) 2003-2008 TU/e Eindhoven       *
 *                and is licensed under the                *
 *            Common Public License, Version 1.0           *
 *        by Eindhoven University of Technology            *
 *           Department of Information Systems             *
 *                 http://is.tm.tue.nl                     *
 *                                                         *
 **********************************************************/

package org.processmining.converting;

import org.processmining.converting.*;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.mining.MiningResult;
import org.processmining.framework.models.petrinet.*;
import java.util.*;
import org.processmining.mining.petrinetmining.PetriNetResult;
import org.processmining.framework.models.pdm.*;
import org.processmining.framework.log.LogEvent;
import org.processmining.framework.ui.Message;

/**
 * <p>
 * Title: PDMtoPMG
 * </p>
 * *
 * <p>
 * Description:
 * </p>
 * *
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * *
 * <p>
 * Company: TU/e and Pallas Athena
 * </p>
 * *
 * 
 * @author Johfra Kamphuis
 * @version 1.0
 */

public class PDMtoPMG implements ConvertingPlugin {
	public PDMtoPMG() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getName() {
		return "Product Data Model to Process Model algorithm Golf";
	}

	public String getHtmlDescription() {
		return "http://is.tm.tue.nl/staff/ivanderfeesten/ProM/documentation/PDM2PM.htm";
	}

	public MiningResult convert(ProvidedObject object) {
		PDMModel model = null;
		for (int i = 0; model == null && i < object.getObjects().length; i++) {
			if (object.getObjects()[i] instanceof PDMModel) {
				model = (PDMModel) object.getObjects()[i];
			}
		}

		if (model == null) {
			return null;
		}

		PetriNet petrinet = convert(model);
		return new PetriNetResult(petrinet);
	}

	public PetriNet convert(PDMModel model) {
		PetriNet result = new PetriNet();
		PDMDataElement root = model.getRootElement(); // the "root element of
		// the PDM

		HashMap places = new HashMap(); // set of places (P)
		HashMap transitions = new HashMap(); // set of transitions (T)
		HashSet edges = new HashSet(); // set of edges (F)

		HashMap done = new HashMap();

		// Initialising step, create start and finish place
		Place ps = new Place("P_{}", result);
		places.put(ps.getIdentifier(), ps);
		result.addPlace(ps);
		// Mark the start place
		// Token to = new Token();
		// Ps.addToken(to);
		Place pf = new Place("P_f", result);
		places.put(pf.getIdentifier(), pf);
		result.addPlace(pf);

		// Determine all possible paths through the PDM.
		HashMap paths = getOpaths(model, root);

		Object[] pathsArray = paths.values().toArray();
		for (int c = 0; c < pathsArray.length; c++) {
			// for each path that is determined, do the following:
			HashMap path = new HashMap();
			path = (HashMap) pathsArray[c];

			// Run calculate on the currently selected path.
			// done is empty
			result = calculate(model, result, places, transitions, edges, path,
					done);

			// The entire path is translated.
			// Only a last "empty" transition to the end place P_f needs to be
			// added.
			// This transition goes from P_path to P_f.
			Object[] Opath = path.values().toArray();

			Object[] PPath = new Object[Opath.length];
			for (int ipath = 0; ipath < Opath.length; ipath++) {
				PDMOperation Operation = model
						.getOperation((String) Opath[ipath]);
				/**/
				/**/
				// Note the hard-coded 0 below!
				// It can be used since Operation.getOutputElements().size() ==
				// 1.
				/**/
				/**/
				PDMDataElement Element = (PDMDataElement) Operation
						.getOutputElements().values().toArray()[0];
				PPath[ipath] = Element.getID();
			}

			Object[] pathsort = bubblesort(PPath);
			String ppathname = "{";
			for (int ipath = 0; ipath < pathsort.length; ipath++) {
				ppathname = ppathname + pathsort[ipath].toString();
				if (ipath < (pathsort.length - 1)) {
					ppathname = ppathname + ", ";
				}
			}
			ppathname = ppathname + "}";
			Place ppath = (Place) places.get("P_" + ppathname);

			Transition tloos = new Transition("T_", result);
			transitions.put(tloos.getIdentifier(), tloos);
			result.addTransition(tloos);
			LogEvent fakeloos = new LogEvent(tloos.getIdentifier(), "complete");
			tloos.setLogEvent(fakeloos);

			PNEdge end0 = new PNEdge(ppath, tloos);
			edges.add(end0);
			result.addEdge(end0);
			PNEdge end1 = new PNEdge(tloos, pf);
			edges.add(end1);
			result.addEdge(end1);

		}

		printTestOutput(result);
		return result;
	}

	public PetriNet calculate(PDMModel model, PetriNet result, HashMap places,
			HashMap transitions, HashSet edges, HashMap todo, HashMap done) {

		Object[] opArray = todo.values().toArray();
		for (int j = 0; j < opArray.length; j++) {

			PDMOperation op = model.getOperation((String) opArray[j]);
			HashMap inputs = op.getInputElements();
			HashMap outputs = op.getOutputElements();

			if (issubset(done, inputs)) {
				// Operation opArray[j] (i, cs) element of todo and with cs
				// subset of done is found

				// First create todo_after = (todo \ i) and done_after = (done u
				// i)
				HashMap done_after = new HashMap();
				HashMap todo_after = new HashMap();

				PDMDataElement dataout;
				if (outputs.size() == 1) {
					// Select the correct place
					Object[] outs = outputs.values().toArray();
					/**/
					/**/
					// Note the hard-coded 0 below!
					// It can be used since outputs.size() == 1.
					/**/
					/**/
					dataout = (PDMDataElement) outs[0];

					Object[] doneloop = done.values().toArray();
					for (int dl = 0; dl < doneloop.length; dl++) {
						done_after.put(done_after.size(), doneloop[dl]);
					}
					done_after.put(done_after.size(), dataout.getID());

					Object[] todoloop = todo.values().toArray();
					for (int tl = 0; tl < todoloop.length; tl++) {
						if ((todoloop[tl] == op.getID()) == false) {
							todo_after.put(todo_after.size(), todoloop[tl]);
						}
					}

				} else
					Message
							.add(
									"This should never appear! There is an operation with more than 1 output element!",
									Message.ERROR);
				// todo_after = (todo \ i) and done_after = (done u i) are now
				// created.

				// Check to see if place P_done_after exists, and if not create
				// it!
				// First we sort the values from done_after
				Object[] sort_after = bubblesort(done_after.values().toArray());

				// The sorted result is put in the string pdoneaftername
				String pdoneaftername = "P_{";
				for (int aftersort = 0; aftersort < sort_after.length; aftersort++) {
					pdoneaftername = pdoneaftername
							+ sort_after[aftersort].toString();
					if (aftersort < (sort_after.length - 1)) {
						pdoneaftername = pdoneaftername + ", ";
					}
				}
				pdoneaftername = pdoneaftername + "}";

				// Then check if the place already exists, and if not, create
				// it.
				Place pdoneafter;
				if ((places.containsKey(pdoneaftername)) == false) {
					pdoneafter = new Place(pdoneaftername, result);
					places.put(pdoneafter.getIdentifier(), pdoneafter);
					result.addPlace(pdoneafter);
				} else {
					pdoneafter = (Place) places.get(pdoneaftername);
				}

				// Pdone always exists!
				Object[] sort = bubblesort(done.values().toArray());
				// We put the sorted result in the string pdonename
				String pdonename = "P_{";
				for (int s1 = 0; s1 < sort.length; s1++) {
					pdonename = pdonename + sort[s1].toString();
					if (s1 < (sort.length - 1)) {
						pdonename = pdonename + ", ";
					}
				}
				pdonename = pdonename + "}";
				Place pdone = (Place) places.get(pdonename);

				// Check if P_done and P_done_after are connected
				Boolean connected = true;
				if ((transitions.containsKey("T_" + op.getID())) == false) {
					// The connecting transition does not exist, so there is no
					// connection!
					connected = false;
				} else {
					// The connecting transition exists. But is this true for
					// the edges?
					Transition tconn = (Transition) transitions.get("T_"
							+ op.getID());

					PNEdge econ0 = new PNEdge(pdone, tconn);
					PNEdge econ1 = new PNEdge(tconn, pdoneafter);
					Object[] egs = edges.toArray();
					Boolean econ0exists = false;
					Boolean econ1exists = false;
					for (int e = 0; e < egs.length; e++) {
						if ((egs[e].toString().equals(econ0.toString())) == true) {
							econ0exists = true;
						}
						if ((egs[e].toString().equals(econ1.toString())) == true) {
							econ1exists = true;
						}
					}
					if (econ1exists && econ0exists) {
						// Both edge (pdone -> tconnected) and (tconnected ->
						// pdoneafter) exist
						// Nothing happenes.
					} else {
						// P_done and P_done_after are not connected via
						// transition T_op
						connected = false;
					}
				}

				if (connected == false) {
					// the connecting transition does not exist, so create:
					// - the transition
					// - the edge connecting P_done with the transition
					// - the edge connecting the transition with P_done_after
					Transition tconnect = new Transition("T_" + op.getID(),
							result);
					transitions.put(tconnect.getIdentifier(), tconnect);
					result.addTransition(tconnect);
					LogEvent fakeconnect = new LogEvent(tconnect
							.getIdentifier(), "complete");
					tconnect.setLogEvent(fakeconnect);

					PNEdge e0 = new PNEdge(pdone, tconnect);
					edges.add(e0);
					result.addEdge(e0);

					PNEdge e1 = new PNEdge(tconnect, pdoneafter);
					edges.add(e1);
					result.addEdge(e1);
				}

				result = calculate(model, result, places, transitions, edges,
						todo_after, done_after);
			}
		}
		return result;
	}

	public Boolean issubset(HashMap set, HashMap subset) {
		// This function checks is subset is a subset of set.
		// Returnvalue = true if this is the case.
		// Returnvalue = false if subset isn't a subset of set.
		Boolean returnvalue = true;
		if (subset.isEmpty()) {
			// Do nothing, the empty set IS a subset of any set!
		} else {

			Object[] subs = subset.values().toArray();

			for (int k = 0; k < subs.length; k++) {
				PDMDataElement sub = (PDMDataElement) subs[k];
				if (set.containsValue(sub.getID()) == false) {
					returnvalue = false;
				}
			}
		}
		return returnvalue;
	}

	public Object[] bubblesort(Object[] tosort) {
		// Sort the array tosort using a simple (though inefficient) sorting
		// algorithm.
		Boolean sorting;
		int upperlimit = tosort.length - 1;
		do {
			sorting = false;
			for (int s0 = 0; s0 < upperlimit; s0++) {
				if (tosort[s0].toString().compareTo(tosort[s0 + 1].toString()) < 0) {
					// tosort[s0] comes before tosort[s0 + 1]
					// Do nothing
				} else if (tosort[s0].toString().compareTo(
						tosort[s0 + 1].toString()) == 0) {
					// tosort[s0] en tosort[s0 + 1] are equal
					// Elements in the provided array are supposed to be unique!
					// However, since dealing with an array, not a set,
					// duplicates are possible.
					// So we remove the duplicate!

					Object[] tosortnew = new Object[tosort.length - 1];
					for (int tmp = 0; tmp < s0; tmp++) {
						tosortnew[tmp] = tosort[tmp];
					}
					for (int tmp = s0; tmp < tosortnew.length; tmp++) {
						tosortnew[tmp] = tosort[tmp + 1];
					}
					tosort = tosortnew;
					upperlimit = upperlimit - 1;
					s0 = s0 - 1;

				} else if (tosort[s0].toString().compareTo(
						tosort[s0 + 1].toString()) > 0) {
					// tosort[s0] comes after tosort[s0 + 1]
					// Swap the elements
					String swap = (String) tosort[s0];
					tosort[s0] = tosort[s0 + 1];
					tosort[s0 + 1] = swap;
					sorting = true;
				}
			}
			upperlimit = upperlimit - 1;
		} while (sorting);
		return tosort;
	}

	public HashMap getOpaths(PDMModel model, PDMDataElement ie) {
		// Determines all possible paths in model towards element ie.
		// A path is exactly the set of operations needed to create ie (not a
		// unique set!).
		// No superfluous information will be part of a path.
		// There can be multiple paths towards an information element.
		// ALL POSSIBLE PATHS towards ie are determined.

		Object[] opArray = model.getOperationsWithOutputElement(ie).toArray();
		HashMap returnpaths = new HashMap();

		if (opArray.length == 0) {
			/**/
			/**/
			// This code will never be executed. By implementation, each
			// information element
			// has at least one operation that creates it.
			Message.add("This should never appear!", Message.DEBUG);
			/**/
			/**/
		} else {
			// There is at least one operation (i, cs) that generates ie

			for (int j = 0; j < opArray.length; j++) {
				// For each operation op that generates ie do
				PDMOperation currentoperation = (PDMOperation) opArray[j];

				HashMap currentpath = new HashMap();
				currentpath.put(currentpath.size(), currentoperation.getID());
				HashMap paths = new HashMap();
				paths.put(paths.size(), currentpath);

				HashMap inputs = currentoperation.getInputElements();
				Object[] ins = inputs.values().toArray();
				for (int k = 0; k < ins.length; k++) {
					// For each element from cs do
					PDMDataElement data1 = (PDMDataElement) ins[k];

					HashMap ancestorpaths = new HashMap();
					ancestorpaths = getOpaths(model, data1);
					paths = pathjoin(paths, ancestorpaths);
				}

				Object[] pathsArray = paths.values().toArray();
				for (int l = 0; l < pathsArray.length; l++) {
					HashMap value = new HashMap();
					value = (HashMap) pathsArray[l];
					returnpaths.put(returnpaths.size(), value);
				}
			}
		}
		return returnpaths;
	}

	public HashMap pathjoin(HashMap paths1, HashMap paths2) {
		// Both paths1 and paths2 are HashMaps of HashMaps of elements.
		// If paths1 = {{1},{2}} and paths2 = {{3,4},{5},{6}}, than
		// pathjoin(paths1, paths2) = {{1,3,4},{1,5},{1,6},{2,3,4},{2,5},{2,6}}
		HashMap returnpaths = new HashMap();

		Object[] path1Array = paths1.values().toArray();
		Object[] path2Array = paths2.values().toArray();
		for (int j = 0; j < path1Array.length; j++) {
			for (int k = 0; k < path2Array.length; k++) {
				HashMap currentjoin = new HashMap();

				HashMap path1 = new HashMap();
				path1 = (HashMap) path1Array[j];
				Object[] path1subArray = path1.values().toArray();
				for (int p = 0; p < path1subArray.length; p++) {
					currentjoin.put(currentjoin.size(), path1subArray[p]);
				}

				HashMap path2 = new HashMap();
				path2 = (HashMap) path2Array[k];
				Object[] path2subArray = path2.values().toArray();
				for (int q = 0; q < path2subArray.length; q++) {
					currentjoin.put(currentjoin.size(), path2subArray[q]);
				}

				returnpaths.put(returnpaths.size(), currentjoin);
			}
		}
		return returnpaths;
	}

	/**
	 * This function tells the interface which results are accepted by this
	 * Plugin
	 * 
	 * @param original
	 *            The original mining result
	 * @return Whether or not this result is accepted
	 */
	public boolean accepts(ProvidedObject object) {
		for (int i = 0; i < object.getObjects().length; i++) {
			if (object.getObjects()[i] instanceof PDMModel) {
				return true;
			}
		}
		return false;
	}

	private void jbInit() throws Exception {
	}

	protected void printTestOutput(PetriNet model) {
		Message.add("<PDMtoPM Golf>", Message.TEST);
		Message.add("<Number of transitions = " + model.getTransitions().size()
				+ " >", Message.TEST);
		Message.add("<Number of places = " + model.getPlaces().size() + " >",
				Message.TEST);
		Message.add("<Number of arcs = " + model.getEdges().size() + " >",
				Message.TEST);
		Message.add("</PDMtoPM Golf>", Message.TEST);
	}

}
