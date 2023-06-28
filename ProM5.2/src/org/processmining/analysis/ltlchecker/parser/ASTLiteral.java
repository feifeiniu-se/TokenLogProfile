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

/* Generated By:JJTree: Do not edit this line. ASTLiteral.java */

import java.text.SimpleDateFormat;

public class ASTLiteral extends SimpleNode {

	SimpleDateFormat sdf;

	public void setDateParser(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public SimpleDateFormat getDateParser() {
		return this.sdf;
	}

	public ASTLiteral(int id) {
		super(id);
	}

	public ASTLiteral(LTLParser p, int id) {
		super(p, id);
	}

    public String toString(boolean verbose) {
        return super.toString() + ": sdf=" + (sdf == null ? "null" : sdf.toPattern());
    }
}
