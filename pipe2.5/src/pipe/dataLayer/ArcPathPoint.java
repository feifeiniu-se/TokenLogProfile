/*
 * Created on 28-Feb-2004
 * @author Michael Camacho (and whoever wrote the first bit!)
 * @author Edwin Chung 16 Mar 2007: modified the constructor and several other
 * functions so that DataLayer objects can be created outside the GUI
 */
package pipe.dataLayer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import pipe.gui.GuiView;
import pipe.gui.Pipe;
import pipe.gui.Zoomer;
import pipe.gui.undo.UndoableEdit;
import pipe.gui.undo.AddArcPathPointEdit;
import pipe.gui.undo.ArcPathPointTypeEdit;

public class ArcPathPoint extends PetriNetObject {

	public static final boolean STRAIGHT = false;
	public static final boolean CURVED = true;
	private static int SIZE = 3;
	private static int SIZE_OFFSET = 1;

	// The offset in x for the new point resulting from splitting a point
	private final int DELTA = 10;

	private static RectangularShape shape;
	private ArcPath myArcPath;
	private Point2D.Float point = new Point2D.Float();
	private Point2D.Float realPoint = new Point2D.Float();

	private Point2D.Float control1 = new Point2D.Float();
	private Point2D.Float control2 = new Point2D.Float();

	private boolean pointType; // STRAIGHT or CURVED

	private ArcPathPoint() {
		copyPasteable = false; // we can't copy & paste indivial arc points!
		zoom = 100;
	}

	public ArcPathPoint(ArcPath a) {
		this();
		myArcPath = a;
		setPointLocation(0, 0);
	}

	public ArcPathPoint(float x, float y, boolean _pointType, ArcPath a) {
		this();
		myArcPath = a;
		setPointLocation(x, y);
		pointType = _pointType;
	}

	/**
	 * @author Nadeem
	 */
	public ArcPathPoint(Point2D.Float point, boolean _pointType, ArcPath a) {
		this(point.x, point.y, _pointType, a);
	}

	public Point2D.Float getPoint() {
		return point;
	}

	//
	public void setPointLocation(float x, float y) {
		double realX = Zoomer.getUnzoomedValue(x, zoom);
		double realY = Zoomer.getUnzoomedValue(y, zoom);
		realPoint.setLocation(realX, realY);
		point.setLocation(x, y);
		setBounds((int) x - SIZE, (int) y - SIZE, 2 * SIZE + SIZE_OFFSET, 2
				* SIZE + SIZE_OFFSET);
	}

	public boolean getPointType() {
		return pointType;
	}

	public void updatePointLocation() {
		setPointLocation(point.x, point.y);
	}

	public void setPointType(boolean type) {
		if (pointType != type) {
			pointType = type;
			myArcPath.createPath();
			myArcPath.getArc().updateArcPosition();
		}
	}

	public UndoableEdit togglePointType() {
		pointType = !pointType;
		myArcPath.createPath();
		myArcPath.getArc().updateArcPosition();
		return new ArcPathPointTypeEdit(this);
	}

	public void setVisibilityLock(boolean lock) {
		myArcPath.setPointVisibilityLock(lock);
	}

	public double getAngle(Point2D.Float p2) {
		double angle;

		if (point.y <= p2.y) {
			angle = Math.atan((point.x - p2.x) / (p2.y - point.y));
		} else {
			angle = Math.atan((point.x - p2.x) / (p2.y - point.y)) + Math.PI;
		}

		// Needed to eliminate an exception on Windows
		if (point.equals(p2)) {
			angle = 0;
		}
		return angle;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!ignoreSelection) {
			Graphics2D g2 = (Graphics2D) g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_NORMALIZE);

			if (pointType == CURVED) {
				shape = new Ellipse2D.Double(0, 0, 2 * SIZE, 2 * SIZE);
			} else {
				shape = new Rectangle2D.Double(0, 0, 2 * SIZE, 2 * SIZE);
			}

			if (selected) {
				g2.setPaint(Pipe.SELECTION_FILL_COLOUR);
				g2.fill(shape);
				g2.setPaint(Pipe.SELECTION_LINE_COLOUR);
				g2.draw(shape);
			} else {
				g2.setPaint(Pipe.ELEMENT_FILL_COLOUR);
				g2.fill(shape);
				g2.setPaint(Pipe.ELEMENT_LINE_COLOUR);
				g2.draw(shape);
			}
		}
	}

	public int getIndex() {
		for (int i = 0; i < myArcPath.getNumPoints(); i++) {
			if (myArcPath.getPathPoint(i) == this) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * splitPoint() This method is called when the user selects the popup menu
	 * option Split Point on an Arc Point. The method determines the index of
	 * the selected point in the listarray of ArcPathPoints that an arcpath has.
	 * Then then a new point is created BEFORE this one in the list and offset
	 * by a small delta in the x direction.
	 */
	public UndoableEdit splitPoint() {
		int i = getIndex(); // Get the index of this point

		ArcPathPoint newPoint = new ArcPathPoint(point.x + DELTA, point.y,
				pointType, myArcPath);
		myArcPath.insertPoint(i + 1, newPoint);
		myArcPath.getArc().updateArcPosition();
		return new AddArcPathPointEdit(myArcPath.getArc(), newPoint);
	}

	public Point2D.Float getMidPoint(ArcPathPoint target) {
		return new Point2D.Float((target.point.x + point.x) / 2,
				(target.point.y + point.y) / 2);
	}

	public boolean isDeleteable() {
		int i = getIndex();
		return (i > 0 && i != myArcPath.getNumPoints() - 1);
	}

	public void delete() {// Won't delete if only two points left. General
		// delete.
		if (isDeleteable()) {
			if (getArcPath().getArc().isSelected()) {
				return;
			}
			kill();
			myArcPath.updateArc();
		}
	}

	public void kill() { // delete without the safety check :)
		super.removeFromContainer(); // called internally by ArcPoint and parent
		// ArcPath
		myArcPath.deletePoint(this);
	}

	public Point2D.Float getControl1() {
		return control1;
	}

	public Point2D.Float getControl2() {
		return control2;
	}

	public void setControl1(float _x, float _y) {
		control1.x = _x;
		control1.y = _y;
	}

	public void setControl2(float _x, float _y) {
		control2.x = _x;
		control2.y = _y;
	}

	public void setControl1(Point2D.Float p) {
		control1.x = p.x;
		control1.y = p.y;
	}

	public void setControl2(Point2D.Float p) {
		control2.x = p.x;
		control2.y = p.y;
	}

	public ArcPath getArcPath() {
		return myArcPath;
	}

	public void addedToGui() {
		;
	}

	void hidePoint() {
		super.removeFromContainer();
	}

	public PetriNetObject paste(double despX, double despY,
			boolean toAnotherView) {
		return null;
	}

	public PetriNetObject copy() {
		return null;
	}

	public int getLayerOffset() {
		return Pipe.ARC_POINT_LAYER_OFFSET;
	}

	public void translate(int x, int y) {
		this.setPointLocation(point.x + x, point.y + y);
		myArcPath.updateArc();
	}

	public void undelete(DataLayer model, GuiView view) {
		;
	}

	public String getName() {
		return this.getArcPath().getArc().getName() + " - Point "
				+ this.getIndex();
	}

	public void zoomUpdate(int zoom) {
		this.zoom = zoom;
		// change ArcPathPoint's size a little bit when it's zoomed in or zoomed
		// out
		if (zoom > 213) {
			SIZE = 5;
		} else if (zoom > 126) {
			SIZE = 4;
		} else {
			SIZE = 3;
		}
		float x = Zoomer.getZoomedValue(realPoint.x, zoom);
		float y = Zoomer.getZoomedValue(realPoint.y, zoom);
		point.setLocation(x, y);
		setBounds((int) x - SIZE, (int) y - SIZE, 2 * SIZE + SIZE_OFFSET, 2
				* SIZE + SIZE_OFFSET);
	}

}
