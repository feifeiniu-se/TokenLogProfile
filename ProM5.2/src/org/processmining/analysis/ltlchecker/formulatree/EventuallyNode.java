/***********************************************************
 *      This loggenerate is part of the ProM package          *
 *             http://www.processmining.org/               *
 *                                                         *
 *            Copyright (c) 2003-2006 TU/e Eindhoven       *
 *                and is licensed under the                *
 *            Common Public License, Version 1.0           *
 *        by Eindhoven University of Technology            *
 *           Department of Information Systems             *
 *                 http://is.tm.tue.nl                     *
 *                                                         *
 **********************************************************/

package org.processmining.analysis.ltlchecker.formulatree;

import java.util.LinkedList;

import org.processmining.framework.log.ProcessInstance;

/**
 * EventuallyNode is a node class of the formula tree denoting the eventually
 * operator.
 * 
 * @version 0.2
 * @author HT de Beer
 */
public class EventuallyNode extends UnaryNode {

	// FIELDS
	/**
	 * The next value, that is the previous computed value, needed to compute
	 * the value of the current (pi, ate) combination.
	 */
	private boolean next;
	private boolean nextInitialized;

	// CONSTRUCTORS

	public EventuallyNode() {
		super();
		// Added: initial value.
		this.nextInitialized = false;
	}

	// METHODS

	/**
	 * Compute the value of this node, that is te value of this node given the
	 * i-th ate of pi, computed by calling the value method of the child.
	 * 
	 * @param pi
	 *            The current process instance.
	 * @param ate
	 *            The current audit trail entry of pi.
	 * 
	 * @return The value of this node computed by calling the value method of
	 *         the child applied to the operator of this node.
	 */
	public boolean value(ProcessInstance pi, LinkedList ates, int ateNr) {
		nr = ateNr;

		boolean result = false;
		if (ateNr >= ates.size()) {
			result = child.value(pi, ates, ateNr);
		} else {
			if (!nextInitialized) {
				// That is, next is not initialised, so do it now:
				this.next = child.value(pi, ates, ateNr);
				this.nextInitialized = true;
			}
			;
			boolean c = child.value(pi, ates, ateNr);
			result = (c || this.next);
		}
		;
		this.next = result;
		return result;
	}

	protected String getOperator() {
		return "<>";
	}
}
