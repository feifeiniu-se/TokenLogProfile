package pipe.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import pipe.dataLayer.AnnotationNote;
import pipe.dataLayer.Arc;
import pipe.dataLayer.DataLayer;
import pipe.dataLayer.MarkingParameter;
import pipe.dataLayer.Note;
import pipe.dataLayer.Parameter;
import pipe.dataLayer.PetriNetObject;
import pipe.dataLayer.Place;
import pipe.dataLayer.PlaceTransitionObject;
import pipe.dataLayer.RateParameter;
import pipe.dataLayer.Transition;
import pipe.gui.handler.AnimationHandler;
import pipe.gui.handler.AnnotationNoteHandler;
import pipe.gui.handler.ArcHandler;
import pipe.gui.handler.LabelHandler;
import pipe.gui.handler.ParameterHandler;
import pipe.gui.handler.PlaceHandler;
import pipe.gui.handler.TransitionHandler;
import pipe.gui.undo.UndoManager;
import pipe.gui.undo.AddPetriNetObjectEdit;

/**
 * The petrinet is drawn onto this frame.
 */
public class GuiView extends JLayeredPane implements Observer, Printable {

	private boolean netChanged = false;

	private boolean animationmode = false;

	public Arc createArc; // no longer static

	public PlaceTransitionObject createPTO;

	private AnimationHandler animationHandler = new AnimationHandler();

	// When i'm using GNU/Linux, isMetaDown() doesn't return true when I press
	// "Windows key". I don't know if a problem of my configuration or what.
	// metaDown is used in this case
	boolean metaDown = false;

	private SelectionManager selection;

	private UndoManager undoManager;

	private DataLayer model;

	private ArrayList<PetriNetObject> petriNetObjects = new ArrayList();

	private GuiFrame app = CreateGui.getApp();

	private Zoomer zoomControl;

	// flag used in fast mode to know if a new PetriNetObject has been created
	public boolean newPNO = false;

	// flag used in paintComponents() to know if a call to zoom() has been done
	private boolean doSetViewPosition = true;

	// position where the viewport must be set
	private Point viewPosition = new Point(0, 0);

	public GuiView(DataLayer _model) {
		model = _model;
		setLayout(null);
		setOpaque(true);
		setDoubleBuffered(true);
		setAutoscrolls(true);
		setBackground(Pipe.ELEMENT_FILL_COLOUR);

		zoomControl = new Zoomer(100, app);

		MouseHandler handler = new MouseHandler(this, model);

		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		addMouseListener(handler);
		addMouseMotionListener(handler);
		try {
			addMouseWheelListener((MouseWheelListener) handler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		selection = new SelectionManager(this);
		undoManager = new UndoManager(this, model, app);
	}

	public void addNewPetriNetObject(PetriNetObject newObject) {
		if (newObject != null) {
			if (newObject.getMouseListeners().length == 0) {
				if (newObject instanceof Place) {
					LabelHandler labelHandler = new LabelHandler(
							((Place) newObject).getNameLabel(),
							(Place) newObject);
					((Place) newObject).getNameLabel().addMouseListener(
							labelHandler);
					((Place) newObject).getNameLabel().addMouseMotionListener(
							labelHandler);
					((Place) newObject).getNameLabel().addMouseWheelListener(
							labelHandler);

					PlaceHandler placeHandler = new PlaceHandler(this,
							(Place) newObject);
					newObject.addMouseListener(placeHandler);
					newObject.addMouseWheelListener(placeHandler);
					newObject.addMouseMotionListener(placeHandler);
					add(newObject);
				} else if (newObject instanceof Transition) {
					TransitionHandler transitionHandler = new TransitionHandler(
							this, (Transition) newObject);
					LabelHandler labelHandler = new LabelHandler(
							((Transition) newObject).getNameLabel(),
							(Transition) newObject);
					((Transition) newObject).getNameLabel().addMouseListener(
							labelHandler);
					((Transition) newObject).getNameLabel()
							.addMouseMotionListener(labelHandler);
					((Transition) newObject).getNameLabel()
							.addMouseWheelListener(labelHandler);

					newObject.addMouseListener(transitionHandler);
					newObject.addMouseMotionListener(transitionHandler);
					newObject.addMouseWheelListener(transitionHandler);

					newObject.addMouseListener(animationHandler);
					add(newObject);
				} else if (newObject instanceof Arc) {
					add(newObject);
					ArcHandler arcHandler = new ArcHandler(this,
							(Arc) newObject);
					newObject.addMouseListener(arcHandler);
					newObject.addMouseWheelListener(arcHandler);
					newObject.addMouseMotionListener(arcHandler);
				} else if (newObject instanceof AnnotationNote) {
					add(newObject);
					AnnotationNoteHandler noteHandler = new AnnotationNoteHandler(
							this, (AnnotationNote) newObject);
					newObject.addMouseListener(noteHandler);
					newObject.addMouseMotionListener(noteHandler);
					((Note) newObject).getNote().addMouseListener(noteHandler);
					((Note) newObject).getNote().addMouseMotionListener(
							noteHandler);
				} else if (newObject instanceof Parameter) {
					add(newObject);
					ParameterHandler parameterHandler = new ParameterHandler(
							this, (Parameter) newObject);
					newObject.addMouseListener(parameterHandler);
					newObject.addMouseMotionListener(parameterHandler);
					((Parameter) newObject).getNote().addMouseListener(
							parameterHandler);
					((Parameter) newObject).getNote().addMouseMotionListener(
							parameterHandler);
				}
				if (newObject instanceof Zoomable) {
					newObject.zoomUpdate(getZoom());
				}
			}
		}
		validate();
		repaint();
	}

	public void update(Observable o, Object diffObj) {
		if ((diffObj instanceof PetriNetObject) && (diffObj != null)) {
			if (CreateGui.appGui.getMode() == Pipe.CREATING) {

				addNewPetriNetObject((PetriNetObject) diffObj);
			}
			repaint();
		}
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		if (pageIndex > 0) {
			return Printable.NO_SUCH_PAGE;
		}
		Graphics2D g2D = (Graphics2D) g;
		// Move origin to page printing area corner
		g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2D.scale(0.5, 0.5);
		print(g2D); // Draw the net

		return Printable.PAGE_EXISTS;
	}

	/**
	 * This method is called whenever the frame is moved, resized etc. It
	 * iterates over the existing petrinet objects and repaints them. TODO:
	 * write a better description than this since it is now totally not
	 * happening.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (Grid.isEnabled()) {
			Grid.updateSize(this);
			Grid.drawGrid(g);
		}

		selection.updateBounds();

		if (doSetViewPosition) {
			((JViewport) getParent()).setViewPosition(viewPosition);
			app.validate();
			doSetViewPosition = false;
		}
	}

	public void updatePreferredSize() {
		// iterate over net objects
		// setPreferredSize() accordingly

		Component[] components = getComponents();
		Dimension d = new Dimension(0, 0);
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == SelectionManager.class) {
				continue; // SelectionObject not included
			}
			Rectangle r = components[i].getBounds();
			int x = r.x + r.width + 20;
			int y = r.y + r.height + 20;
			if (x > d.width) {
				d.width = x;
			}
			if (y > d.height) {
				d.height = y;
			}
		}
		setPreferredSize(d);
		Container parent = getParent();
		if (parent != null) {
			parent.validate();
		}
	}

	public void changeAnimationMode(boolean status) {
		animationmode = status;
	}

	public void setCursorType(String type) {
		if (type.equals("arrow")) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (type.equals("crosshair")) {
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		} else if (type.equals("move")) {
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
	}

	public SelectionManager getSelectionObject() {
		return selection;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public Zoomer getZoomController() {
		return zoomControl;
	}

	public void zoom() {
		Component[] children = getComponents();

		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Zoomable) {
				((Zoomable) children[i]).zoomUpdate(zoomControl.getPercent());
			}
		}
		doSetViewPosition = true;
	}

	public void add(PetriNetObject pnObject) {
		setLayer(pnObject, DEFAULT_LAYER.intValue() + pnObject.getLayerOffset());
		super.add(pnObject);
		pnObject.addedToGui();
		petriNetObjects.add(pnObject);
	}

	//
	public void setMetaDown(boolean down) {
		metaDown = down;
	}

	public Point getPointer() {
		return getMousePosition();
	}

	public AnimationHandler getAnimationHandler() {
		return animationHandler;
	}

	public boolean isInAnimationMode() {
		return animationmode;
	}

	public boolean getNetChanged() {
		return netChanged;
	}

	public void setNetChanged(boolean _netChanged) {
		netChanged = _netChanged;
	}

	public ArrayList<PetriNetObject> getPNObjects() {
		return petriNetObjects;
	}

	public void remove(Component comp) {
		boolean result = petriNetObjects.remove(comp);
		// if (result) {
		// System.out.println("DEBUG: remove PNO from view");
		// /}
		super.remove(comp);
	}

	public void drag(Point dragStart, Point dragEnd) {
		if (dragStart == null) {
			return;
		}
		JViewport viewer = (JViewport) getParent();
		Point offScreen = viewer.getViewPosition();
		if (dragStart.x > dragEnd.x) {
			offScreen.translate(viewer.getWidth(), 0);
		}
		if (dragStart.y > dragEnd.y) {
			offScreen.translate(0, viewer.getHeight());
		}
		offScreen.translate(dragStart.x - dragEnd.x, dragStart.y - dragEnd.y);
		Rectangle r = new Rectangle(offScreen.x, offScreen.y, 1, 1);
		scrollRectToVisible(r);
	}

	private Point midpoint(int zoom) {
		JViewport viewport = (JViewport) getParent();
		double midpointX = Zoomer.getUnzoomedValue(viewport.getViewPosition().x
				+ (viewport.getWidth() * 0.5), zoom);
		double midpointY = Zoomer.getUnzoomedValue(viewport.getViewPosition().y
				+ (viewport.getHeight() * 0.5), zoom);
		return (new java.awt.Point((int) midpointX, (int) midpointY));
	}

	public void zoomIn() {
		int zoom = zoomControl.getPercent();
		if (zoomControl.zoomIn()) {
			zoomTo(midpoint(zoom));
		}
	}

	public void zoomOut() {
		int zoom = zoomControl.getPercent();
		if (zoomControl.zoomOut()) {
			zoomTo(midpoint(zoom));
		}
	}

	public void zoomTo(Point point) {
		// The zoom is not as smooth as it should be. As far I know, this
		// behavior
		// is caused when the method setSize() is called in NameLabel's
		// updateSize()
		// In order to disguise it, the view is hidden and a white layer is
		// shown.
		// I know it's not a smart solution...
		// I think zoom function should be redone from scratch so that
		// BlankLayer
		// class and doSetViewPosition could be removed

		int zoom = zoomControl.getPercent();

		JViewport viewport = (JViewport) getParent();

		double currentXNoZoom = Zoomer.getUnzoomedValue(viewport
				.getViewPosition().x
				+ (viewport.getWidth() * 0.5), zoom);
		double newZoomedX = Zoomer.getZoomedValue(point.x, zoom);
		double newZoomedY = Zoomer.getZoomedValue(point.y, zoom);

		int newViewX = (int) (newZoomedX - (viewport.getWidth() * 0.5));
		if (newViewX < 0) {
			newViewX = 0;
		}

		int newViewY = (int) (newZoomedY - (viewport.getHeight() * 0.5));
		if (newViewY < 0) {
			newViewY = 0;
		}

		// if (doSetViewPosition) {
		viewPosition.setLocation(newViewX, newViewY);
		viewport.setViewPosition(viewPosition);
		// }

		zoom();

		app.hideNet(true); // hide current view :-(

		updatePreferredSize();
	}

	public int getZoom() {
		return zoomControl.getPercent();
	}

	class MouseHandler extends MouseInputAdapter {

		private PetriNetObject pnObject;

		private GuiView view;

		private DataLayer model;

		private Point dragStart;

		public MouseHandler(GuiView _view, DataLayer _model) {
			super();
			view = _view;
			model = _model;
		}

		private Point adjustPoint(Point p, int zoom) {
			int offset = (int) (Zoomer.getScaleFactor(zoom)
					* Pipe.PLACE_TRANSITION_HEIGHT / 2);

			int x = Zoomer.getUnzoomedValue(p.x - offset, zoom);
			int y = Zoomer.getUnzoomedValue(p.y - offset, zoom);

			p.setLocation(x, y);
			return p;
		}

		private PlaceTransitionObject newPlace(Point p) {
			p = adjustPoint(p, view.getZoom());

			pnObject = new Place(Grid.getModifiedX(p.x), Grid.getModifiedY(p.y));
			model.addPetriNetObject(pnObject);
			view.addNewPetriNetObject(pnObject);
			return (PlaceTransitionObject) pnObject;
		}

		private PlaceTransitionObject newTransition(Point p, boolean timed) {
			p = adjustPoint(p, view.getZoom());

			pnObject = new Transition(Grid.getModifiedX(p.x), Grid
					.getModifiedY(p.y));
			((Transition) pnObject).setTimed(timed);
			model.addPetriNetObject(pnObject);
			view.addNewPetriNetObject(pnObject);
			return (PlaceTransitionObject) pnObject;
		}

		public void mousePressed(MouseEvent e) {
			Point start = e.getPoint();
			Point p;

			if (SwingUtilities.isLeftMouseButton(e)) {
				int mode = app.getMode();
				switch (mode) {
				case Pipe.PLACE:
					PlaceTransitionObject pto = newPlace(e.getPoint());
					getUndoManager().addNewEdit(
							new AddPetriNetObjectEdit(pto, view, model));
					if (e.isControlDown()) {
						app.setFastMode(Pipe.FAST_TRANSITION);
						pnObject.dispatchEvent(e);
					}
					break;

				case Pipe.IMMTRANS:
				case Pipe.TIMEDTRANS:
					boolean timed = (mode == Pipe.TIMEDTRANS ? true : false);
					pto = newTransition(e.getPoint(), timed);
					getUndoManager().addNewEdit(
							new AddPetriNetObjectEdit(pto, view, model));
					if (e.isControlDown()) {
						app.setFastMode(Pipe.FAST_PLACE);
						pnObject.dispatchEvent(e);
					}
					break;

				case Pipe.ARC:
				case Pipe.INHIBARC:
					// Add point to arc in creation
					if (createArc != null) {
						addPoint(createArc, e);
					}
					break;

				case Pipe.ANNOTATION:
					p = adjustPoint(e.getPoint(), view.getZoom());

					pnObject = new AnnotationNote(p.x, p.y);
					model.addPetriNetObject(pnObject);
					view.addNewPetriNetObject(pnObject);
					getUndoManager().addNewEdit(
							new AddPetriNetObjectEdit(pnObject, view, model));
					((AnnotationNote) pnObject).enableEditMode();
					break;

				case Pipe.RATE:
					try {
						String label = JOptionPane.showInputDialog(
								"Rate Parameter Label:", "");
						if (label == null) {
							// User has chosen "Cancel", nothing to be done
							break;
						}

						if (label.length() == 0) {
							throw new Exception("label Incorrecte");
						} else if (model.existsRateParameter(label)) {
							throw new Exception("label Already Defined");
						}

						String value = JOptionPane.showInputDialog(
								"Rate Parameter Value:", "");

						p = adjustPoint(e.getPoint(), view.getZoom());

						pnObject = new RateParameter(label, Double
								.parseDouble(value), p.x, p.y);
						model.addPetriNetObject(pnObject);
						view.addNewPetriNetObject(pnObject);
						getUndoManager()
								.addNewEdit(
										new AddPetriNetObjectEdit(pnObject,
												view, model));
					} catch (java.lang.NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Enter a rate",
								"Invalid entry", JOptionPane.ERROR_MESSAGE);
					} catch (Exception exc) {
						String message = exc.getMessage();
						if (message == null) {
							message = "Unknown Error!";
						}
						JOptionPane.showMessageDialog(null, message,
								"Invalid entry", JOptionPane.ERROR_MESSAGE);
					}
					break;

				case Pipe.MARKING:
					try {
						String label = JOptionPane.showInputDialog(
								"Marking Parameter Label:", "");
						if (label == null) {
							// User has chosen "Cancel", nothing to be done
							break;
						}

						if (label.length() == 0) {
							throw new Exception("Please, enter a name");
						} else if (model.existsMarkingParameter(label)) {
							throw new Exception("Parameter already defined");
						}
						String value = JOptionPane.showInputDialog(
								"Marking Parameter Value:", "");

						p = adjustPoint(e.getPoint(), view.getZoom());

						pnObject = new MarkingParameter(label, Integer
								.parseInt(value), p.x, p.y);
						model.addPetriNetObject(pnObject);
						view.addNewPetriNetObject(pnObject);
						getUndoManager()
								.addNewEdit(
										new AddPetriNetObjectEdit(pnObject,
												view, model));
					} catch (java.lang.NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null,
								"Enter a positive integer", "Invalid entry",
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception exc) {
						String message = exc.getMessage();
						if (message == null) {
							message = "Unknown Error!";
						}
						JOptionPane.showMessageDialog(null, message,
								"Invalid entry", JOptionPane.ERROR_MESSAGE);
					}
					break;

				case Pipe.FAST_PLACE:
					if (e.isMetaDown() || metaDown) { // provisional
						if (createArc != null) {
							addPoint(createArc, e);
						}
					} else {
						if (createArc == null) {
							break;
						}
						// user has not clicked on an old PetriNetObject, so
						// a new PNO must be created
						view.newPNO = true;

						createPTO = newPlace(e.getPoint());
						getUndoManager().addNewEdit(
								new AddPetriNetObjectEdit(createPTO, view,
										model));
						pnObject.getMouseListeners()[0].mouseReleased(e);
						if (e.isControlDown()) {
							// keep "fast mode"
							app.setMode(Pipe.FAST_TRANSITION);
							pnObject.getMouseListeners()[0].mousePressed(e);
						} else {
							// exit "fast mode"
							app.resetMode();
						}
					}
					break;

				case Pipe.FAST_TRANSITION:
					if (e.isMetaDown() || metaDown) { // provisional
						if (createArc != null) {
							addPoint(createArc, e);
						}
					} else {
						if (createArc == null) {
							break;
						}
						// user has not clicked on an old PetriNetObject, so
						// a new PNO must be created
						view.newPNO = true;

						createPTO = newTransition(e.getPoint(), e.isAltDown());
						getUndoManager().addNewEdit(
								new AddPetriNetObjectEdit(createPTO, view,
										model));
						pnObject.getMouseListeners()[0].mouseReleased(e);
						if (e.isControlDown()) {
							// keep "fast mode"
							app.setMode(Pipe.FAST_PLACE);
							pnObject.getMouseListeners()[0].mousePressed(e);
						} else {
							// exit "fast mode"
							app.resetMode();
						}
					}
					break;

				case Pipe.DRAG:
					dragStart = new Point(start);
					break;

				default:
					break;
				}
			} else {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				dragStart = new Point(start);
			}
			updatePreferredSize();
		}

		private void addPoint(final Arc createArc, final MouseEvent e) {
			int x = Grid.getModifiedX(e.getX());
			int y = Grid.getModifiedY(e.getY());
			boolean shiftDown = e.isShiftDown();
			createArc.setEndPoint(x, y, shiftDown);
			createArc.getArcPath().addPoint(x, y, shiftDown);
		}

		public void mouseReleased(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}

		public void mouseMoved(MouseEvent e) {
			if (createArc != null) {
				createArc.setEndPoint(Grid.getModifiedX(e.getX()), Grid
						.getModifiedY(e.getY()), e.isShiftDown());
			}
		}

		/**
		 * @see javax.swing.event.MouseInputAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {
			// if (CreateGui.getApp().getMode() == Pipe.DRAG){
			view.drag(dragStart, e.getPoint());
			// }
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			if (!e.isControlDown()) {
				return;
			} else {
				if (e.getWheelRotation() > 0) {
					view.zoomIn();
				} else {
					view.zoomOut();
				}
			}
		}

	}

}
