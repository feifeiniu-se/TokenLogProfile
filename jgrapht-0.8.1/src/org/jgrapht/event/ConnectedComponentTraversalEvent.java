/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * This library is free loggenerate; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -------------------------------------
 * ConnectedComponentTraversalEvent.java
 * -------------------------------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: ConnectedComponentTraversalEvent.java 487 2006-07-02 00:53:17Z
 * perfecthash $
 *
 * Changes
 * -------
 * 11-Aug-2003 : Initial revision (BN);
 *
 */
package org.jgrapht.event;

import java.util.*;

/**
 * A traversal event with respect to a connected component.
 * 
 * @author Barak Naveh
 * @since Aug 11, 2003
 */
public class ConnectedComponentTraversalEvent extends EventObject {
	// ~ Static fields/initializers
	// ---------------------------------------------

	private static final long serialVersionUID = 3834311717709822262L;

	/**
	 * Connected component traversal started event.
	 */
	public static final int CONNECTED_COMPONENT_STARTED = 31;

	/**
	 * Connected component traversal finished event.
	 */
	public static final int CONNECTED_COMPONENT_FINISHED = 32;

	// ~ Instance fields
	// --------------------------------------------------------

	/**
	 * The type of this event.
	 */
	private int type;

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Creates a new ConnectedComponentTraversalEvent.
	 * 
	 * @param eventSource
	 *            the source of the event.
	 * @param type
	 *            the type of event.
	 */
	public ConnectedComponentTraversalEvent(Object eventSource, int type) {
		super(eventSource);
		this.type = type;
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * Returns the event type.
	 * 
	 * @return the event type.
	 */
	public int getType() {
		return type;
	}
}

// End ConnectedComponentTraversalEvent.java
