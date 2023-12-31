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

/* Generated By:JJTree: Do not edit this line. ASTStringList.java */

import java.util.ArrayList;
import java.util.TreeSet;

public class ASTStringList extends SimpleNode {

	/** Strings is a container of all the strings is the set. */
	TreeSet strings;

//    public ASTStringList() {
//	super();
//	strings = new ArrayList();
//   }

	public void setStrings(TreeSet strings) {
		this.strings = strings;
	}

	public TreeSet getStrings() {
		return this.strings;
	}

	public ASTStringList(int id) {
		super(id);
	}

	public ASTStringList(LTLParser p, int id) {
		super(p, id);
	}

    public String toString(boolean verbose) {
        return super.toString() + ": strings=" + strings;
    }

	public void setStrings(ArrayList strings2) {
		// TODO Auto-generated method stub
		
	}
}
