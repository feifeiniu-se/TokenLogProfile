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

package org.processmining.analysis.ltlchecker.parser;

/* Generated By:JJTree: Do not edit this line. ASTQuantification.java */

public class ASTQuantification extends SimpleNode {

	private Attribute dummy;

	public void setDummy(Attribute dummy) {
		this.dummy = dummy;
	}

	public Attribute getDummy() {
		return this.dummy;
	}

	public ASTQuantification(int id) {
		super(id);
	}

	public ASTQuantification(LTLParser p, int id) {
		super(p, id);
	}

    public String toString(boolean verbose) {
        return super.toString() + ": dummy=" + (dummy == null ? "null" : dummy.toString(verbose));
    }
    
	public String asParseableString() {
		String result = "";
		
		switch (getType()) {
		case FORALL: result = "forall"; break;
		case EXISTS: result = "exists"; break;
		default:
			assert(false);
		};
		
		assert(children != null);
		assert(children.length == 2);
		assert(children[0] != null);
		assert(children[1] != null);
		
		result += " [ " + dummy.asParseableArgument() + " | ";
		
		SimpleNode n = (SimpleNode) children[1];
		result += n.asParseableString();
		
		result += " ] ";
		return result;
	}
}
