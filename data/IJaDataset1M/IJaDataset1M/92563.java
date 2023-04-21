package org.nakedobjects.plugins.dndviewer.viewer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dndviewer.Background;
import org.nakedobjects.plugins.dndviewer.Canvas;
import org.nakedobjects.plugins.dndviewer.Click;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.Drag;
import org.nakedobjects.plugins.dndviewer.DragStart;
import org.nakedobjects.plugins.dndviewer.FocusManager;
import org.nakedobjects.plugins.dndviewer.HelpViewer;
import org.nakedobjects.plugins.dndviewer.InteractionSpy;
import org.nakedobjects.plugins.dndviewer.Toolkit;
import org.nakedobjects.plugins.dndviewer.UserActionSet;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.ViewAreaType;
import org.nakedobjects.plugins.dndviewer.ViewRequirement;
import org.nakedobjects.plugins.dndviewer.Viewer;
import org.nakedobjects.plugins.dndviewer.viewer.basic.PopupMenuContainer;
import org.nakedobjects.plugins.dndviewer.viewer.border.BackgroundBorder;
import org.nakedobjects.plugins.dndviewer.viewer.border.LineBorder;
import org.nakedobjects.plugins.dndviewer.viewer.debug.DebugFrame;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Location;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Size;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Text;
import org.nakedobjects.plugins.dndviewer.viewer.notifier.ViewUpdateNotifier;
import org.nakedobjects.plugins.dndviewer.viewer.undo.UndoStack;
import org.nakedobjects.plugins.dndviewer.viewer.util.Properties;
import org.nakedobjects.plugins.dndviewer.viewer.view.message.MessageContent;
import org.nakedobjects.plugins.dndviewer.viewer.view.simple.NullView;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class XViewer implements Viewer {

    private static final Size NO_SIZE = new Size(0, 0);

    private static final Logger LOG = Logger.getLogger(Viewer.class);

    private static final Logger UI_LOG = Logger.getLogger("ui." + Viewer.class.getName());

    private static final LoggingOptions LOGGING_OPTIONS = new LoggingOptions();

    private static final NullView CLEAR_OVERLAY = new NullView();

    private static final Bounds NO_REDRAW = new Bounds();

    private ApplicationOptions APPLICATION_OPTIONS;

    private final DebugOptions DEBUG_OPTIONS = new DebugOptions(this);

    private Graphics bufferGraphics;

    private Image doubleBuffer;

    private boolean doubleBuffering = false;

    private Insets insets;

    private Size internalDisplaySize = new Size(1, 1);

    private ShutdownListener listener;

    private View overlayView;

    private final Bounds redrawArea;

    private int redrawCount = 100000;

    private RenderingArea renderingArea;

    private View rootView;

    private String status;

    private boolean runningAsExploration;

    boolean showExplorationMenuByDefault;

    boolean showRepaintArea;

    private InteractionSpy spy;

    private int statusBarHeight;

    private final UndoStack undoStack = new UndoStack();

    protected ViewUpdateNotifier updateNotifier;

    private KeyboardManager keyboardManager;

    private HelpViewer helpViewer;

    private Background background;

    private Bounds statusBarArea;

    private XFeedbackManager feedbackManager;

    private boolean refreshStatus;

    private static Boolean isDotNetBool;

    private static boolean isDotNet() {
        if (isDotNetBool == null) {
            isDotNetBool = new Boolean(System.getProperty("java.version", "dotnet").equals("dotnet"));
        }
        return isDotNetBool.booleanValue();
    }

    public XViewer() {
        doubleBuffering = NakedObjectsContext.getConfiguration().getBoolean(Properties.PROPERTY_BASE + "double-buffer", true);
        showExplorationMenuByDefault = NakedObjectsContext.getConfiguration().getBoolean(Properties.PROPERTY_BASE + "exploration.show", true);
        overlayView = CLEAR_OVERLAY;
        redrawArea = new Bounds();
    }

    public void addSpyAction(final String actionMessage) {
        if (spy != null) {
            spy.addAction(actionMessage);
        }
    }

    public void addToNotificationList(final View view) {
        updateNotifier.add(view.getView());
    }

    public String selectFilePath(final String title, final String directory) {
        return renderingArea.selectFilePath(title, directory);
    }

    public void setKeyboardFocus(final View view) {
        if (view == null) {
            return;
        }
        final FocusManager currentFocusManager = keyboardManager.getFocusManager();
        if (currentFocusManager != null && currentFocusManager.getFocus() != null && currentFocusManager.getFocus().getParent() != null) {
            currentFocusManager.getFocus().getParent().markDamaged();
        }
        if (currentFocusManager != null) {
            final View currentFocus = currentFocusManager.getFocus();
            if (currentFocus != null && currentFocus != view) {
                currentFocus.focusLost();
            }
        }
        final FocusManager focusManager = view.getFocusManager();
        if (focusManager != null) {
            focusManager.setFocus(view);
            if (view.getParent() != null) {
                view.getParent().markDamaged();
            }
        }
        if (focusManager == null) {
            LOG.warn("No focus manager for " + view);
        } else {
            keyboardManager.setFocusManager(focusManager);
        }
    }

    public void clearOverlayView() {
        overlayView.markDamaged();
        overlayView = CLEAR_OVERLAY;
    }

    public void clearOverlayView(final View view) {
        if (this.getOverlayView() != view) {
            LOG.warn("no such view to remove: " + view);
        }
        this.clearOverlayView();
    }

    public void quit() {
        if (spy != null) {
            spy.close();
        }
        DebugFrame.disposeAll();
        close();
        if (listener != null) {
            listener.quit();
        }
    }

    public void disposeOverlayView() {
        clearOverlayView();
    }

    public void disposeUnneededViews() {
        updateNotifier.removeViewsForDisposedObjects();
    }

    public View dragFrom(final Location location) {
        if (onOverlay(location)) {
            location.subtract(overlayView.getLocation());
            return overlayView.dragFrom(location);
        } else {
            return rootView.dragFrom(location);
        }
    }

    public Drag dragStart(final DragStart start) {
        if (onOverlay(start.getLocation())) {
            start.subtract(overlayView.getLocation());
            return overlayView.dragStart(start);
        } else {
            return rootView.dragStart(start);
        }
    }

    public void firstClick(final Click click) {
        if (onOverlay(click.getLocation())) {
            click.subtract(overlayView.getLocation());
            overlayView.firstClick(click);
        } else {
            rootView.firstClick(click);
        }
    }

    private FocusManager getFocusManager() {
        return overlayView == CLEAR_OVERLAY ? keyboardManager.getFocusManager() : overlayView.getFocusManager();
    }

    public Bounds getOverlayBounds() {
        final Bounds bounds = new Bounds(createSize(renderingArea.getSize()));
        final Insets in = renderingArea.getInsets();
        bounds.contract(in.left + in.right, in.top + in.bottom);
        bounds.contract(0, statusBarHeight);
        return bounds;
    }

    private Size createSize(final Dimension size) {
        return new Size(size.width, size.height);
    }

    public View getOverlayView() {
        return overlayView;
    }

    public InteractionSpy getSpy() {
        return spy;
    }

    public UndoStack getUndoStack() {
        return undoStack;
    }

    public boolean hasFocus(final View view) {
        final FocusManager focusManager = keyboardManager.getFocusManager();
        return focusManager != null && focusManager.getFocus() == view;
    }

    public View identifyView(final Location location, final boolean includeOverlay) {
        if (includeOverlay && onOverlay(location)) {
            location.subtract(overlayView.getLocation());
            return overlayView.identify(location);
        } else {
            return rootView.identify(location);
        }
    }

    public void init() {
        if (updateNotifier == null) {
            throw new NullPointerException("No update notifier set for " + this);
        }
        if (rootView == null) {
            throw new NullPointerException("No root view set for " + this);
        }
        insets = new Insets(0, 0, 0, 0);
        spy = new InteractionSpy(new SpyWindow());
        keyboardManager = new KeyboardManager(this);
        final InteractionHandler interactionHandler = new InteractionHandler(this, feedbackManager, keyboardManager, spy);
        renderingArea.addMouseMotionListener(interactionHandler);
        renderingArea.addMouseListener(interactionHandler);
        renderingArea.addKeyListener(interactionHandler);
        if (NakedObjectsContext.getConfiguration().getBoolean(Properties.PROPERTY_BASE + "show-mouse-spy", false)) {
            spy.open();
        }
        setKeyboardFocus(rootView);
        APPLICATION_OPTIONS = new ApplicationOptions(listener);
    }

    public boolean isRunningAsExploration() {
        return runningAsExploration;
    }

    public boolean isShowingMouseSpy() {
        return spy.isVisible();
    }

    public void markDamaged(final Bounds bounds) {
        if (spy != null) {
            spy.addDamagedArea(bounds);
        }
        synchronized (redrawArea) {
            if (redrawArea.equals(NO_REDRAW)) {
                redrawArea.setBounds(bounds);
                UI_LOG.debug("damage - new area " + redrawArea);
            } else {
                if (!bounds.getSize().equals(NO_SIZE)) {
                    redrawArea.union(bounds);
                    UI_LOG.debug("damage - extend area " + redrawArea + " - to include " + bounds);
                }
            }
        }
    }

    public void menuOptions(final UserActionSet options) {
    }

    public void mouseDown(final Click click) {
        if (onOverlay(click.getLocation())) {
            click.subtract(overlayView.getLocation());
            overlayView.mouseDown(click);
        } else {
            rootView.mouseDown(click);
        }
    }

    public void mouseMoved(final Location location) {
        if (onOverlay(location)) {
            location.subtract(overlayView.getLocation());
            overlayView.mouseMoved(location);
        } else {
            rootView.mouseMoved(location);
        }
    }

    public void mouseUp(final Click click) {
        if (onOverlay(click.getLocation())) {
            click.subtract(overlayView.getLocation());
            overlayView.mouseUp(click);
        } else {
            rootView.mouseUp(click);
        }
    }

    private boolean onOverlay(final Location mouse) {
        return overlayView.getBounds().contains(mouse);
    }

    public void paint(final Graphics graphic) {
        redrawCount++;
        graphic.translate(insets.left, insets.top);
        final Rectangle paintArea = graphic.getClipBounds();
        final Rectangle layoutArea = layoutViews();
        if (layoutArea != null) {
            paintArea.union(layoutArea);
        }
        if (spy != null) {
            spy.redraw(paintArea.toString(), redrawCount);
        }
        if (UI_LOG.isDebugEnabled()) {
            UI_LOG.debug("------ repaint viewer #" + redrawCount + " " + paintArea.x + "," + paintArea.y + " " + paintArea.width + "x" + paintArea.height);
        }
        final Canvas c = createCanvas(graphic, paintArea);
        if (background != null) {
            background.draw(c.createSubcanvas(), rootView.getSize());
        }
        if (rootView != null) {
            rootView.draw(c.createSubcanvas());
        }
        Bounds bounds = overlayView.getBounds();
        if (paintArea.intersects(new Rectangle(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()))) {
            overlayView.draw(c.createSubcanvas(bounds));
        }
        if (doubleBuffering) {
            graphic.drawImage(doubleBuffer, 0, 0, null);
        }
        if (showRepaintArea) {
            graphic.setColor(((AwtColor) Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_REPAINT)).getAwtColor());
            graphic.drawRect(paintArea.x, paintArea.y, paintArea.width - 1, paintArea.height - 1);
            graphic.drawString("#" + redrawCount, paintArea.x + 3, paintArea.y + 15);
        }
        paintStatus(graphic);
    }

    private Canvas createCanvas(final Graphics graphic, final Rectangle paintArea) {
        final int w = internalDisplaySize.getWidth();
        final int h = internalDisplaySize.getHeight();
        if (doubleBuffering) {
            if ((doubleBuffer == null) || (bufferGraphics == null) || (doubleBuffer.getWidth(null) < w) || (doubleBuffer.getHeight(null) < h)) {
                doubleBuffer = renderingArea.createImage(w, h);
                LOG.debug("buffer sized to " + doubleBuffer.getWidth(null) + "x" + doubleBuffer.getHeight(null));
            }
            bufferGraphics = doubleBuffer.getGraphics().create();
        } else {
            bufferGraphics = graphic;
        }
        bufferGraphics.clearRect(paintArea.x, paintArea.y, paintArea.width, paintArea.height);
        bufferGraphics.clearRect(0, 0, w, h);
        bufferGraphics.setClip(paintArea.x, paintArea.y, paintArea.width, paintArea.height);
        final Canvas c = new AwtCanvas(bufferGraphics, renderingArea, paintArea.x, paintArea.y, paintArea.width, paintArea.height);
        return c;
    }

    /**
     * Lays out the invalid views and returns the area to be repainted.
     */
    private Rectangle layoutViews() {
        if (!Thread.currentThread().getName().startsWith("AWT-EventQueue") && !isDotNet()) {
            throw new NakedObjectException("Drawing with wrong thread: " + Thread.currentThread());
        }
        final Size rootViewSize = rootView.getSize();
        overlayView.layout(rootViewSize);
        rootView.layout(rootViewSize);
        synchronized (redrawArea) {
            if (!redrawArea.equals(NO_REDRAW)) {
                final Rectangle r2 = new Rectangle(redrawArea.getX(), redrawArea.getY(), redrawArea.getWidth(), redrawArea.getHeight());
                redrawArea.setBounds(NO_REDRAW);
                return r2;
            }
        }
        return null;
    }

    private void paintStatus(final Graphics graphic) {
        final int height = internalDisplaySize.getHeight();
        final int top = height - statusBarHeight;
        if (refreshStatus || graphic.getClip().getBounds().getY() + graphic.getClip().getBounds().getHeight() > top) {
            refreshStatus = false;
            UI_LOG.debug("changed user status " + status + " " + statusBarArea);
            final int width = internalDisplaySize.getWidth();
            graphic.setClip(0, top, width, statusBarHeight);
            graphic.setColor(((AwtColor) Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3)).getAwtColor());
            final AwtText textStyle = (AwtText) Toolkit.getText(ColorsAndFonts.TEXT_STATUS);
            graphic.setFont(textStyle.getAwtFont());
            final int baseline = top + textStyle.getAscent();
            graphic.fillRect(0, top, width, statusBarHeight);
            graphic.setColor(((AwtColor) Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1)).getAwtColor());
            graphic.drawLine(0, top, internalDisplaySize.getWidth(), top);
            graphic.setColor(((AwtColor) Toolkit.getColor(ColorsAndFonts.COLOR_BLACK)).getAwtColor());
            graphic.drawString(status, 5, baseline + View.VPADDING);
        }
    }

    public View pickupContent(final Location location) {
        if (onOverlay(location)) {
            location.subtract(overlayView.getLocation());
            return overlayView.pickupContent(location);
        } else {
            return rootView.pickupContent(location);
        }
    }

    public View pickupView(final Location location) {
        if (onOverlay(location)) {
            location.subtract(overlayView.getLocation());
            return overlayView.pickupView(location);
        } else {
            return rootView.pickupView(location);
        }
    }

    public void popupMenu(final View over, final Location at, final boolean forView, final boolean includeExploration, final boolean includeDebug) {
        feedbackManager.setBusy(over, null);
        saveCurrentFieldEntry();
        final PopupMenuContainer menu = new PopupMenuContainer(over, at);
        if (over == rootView) {
            menu.addMenuOptions(APPLICATION_OPTIONS);
            menu.addMenuOptions(LOGGING_OPTIONS);
            menu.addMenuOptions(DEBUG_OPTIONS);
        }
        final boolean showExplorationOptions = includeExploration || showExplorationMenuByDefault;
        menu.show(forView, includeDebug, showExplorationOptions);
        feedbackManager.clearBusy(over);
    }

    public void removeFromNotificationList(final View view) {
        updateNotifier.remove(view);
    }

    /**
     * Force a repaint of the damaged area of the viewer.
     */
    public void scheduleRepaint() {
        updateNotifier.invalidateViewsForChangedObjects();
        synchronized (redrawArea) {
            if (!redrawArea.equals(NO_REDRAW) || refreshStatus) {
                UI_LOG.debug("repaint viewer " + redrawArea);
                final Bounds area = new Bounds(redrawArea);
                area.translate(insets.left, insets.top);
                renderingArea.repaint(area.getX(), area.getY(), area.getWidth(), area.getHeight());
                redrawArea.setBounds(NO_REDRAW);
            }
        }
    }

    public void saveCurrentFieldEntry() {
        final FocusManager focusManager = getFocusManager();
        if (focusManager != null) {
            final View focus = focusManager.getFocus();
            if (focus != null) {
                focus.editComplete(false, false);
            }
        }
    }

    public void secondClick(final Click click) {
        if (onOverlay(click.getLocation())) {
            click.subtract(overlayView.getLocation());
            overlayView.secondClick(click);
        } else {
            rootView.secondClick(click);
        }
    }

    public void setBackground(final Background background) {
        this.background = background;
    }

    void setCursor(final Cursor cursor) {
        renderingArea.setCursor(cursor);
    }

    public void setExploration(final boolean asExploration) {
        this.runningAsExploration = asExploration;
    }

    public void setListener(final ShutdownListener listener) {
        this.listener = listener;
    }

    public void setOverlayView(final View view) {
        disposeOverlayView();
        overlayView = view;
        final Size size = view.getRequiredSize(rootView.getSize());
        view.setSize(size);
        view.layout(rootView.getSize());
        view.limitBoundsWithin(getOverlaySize());
        overlayView.markDamaged();
    }

    public Size getOverlaySize() {
        return rootView.getSize();
    }

    public void showInOverlay(Content content, Location location) {
        View view;
        view = Toolkit.getViewFactory().createView(new ViewRequirement(content, ViewRequirement.OPEN));
        view = new LineBorder(2, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2), new BackgroundBorder(Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3), view));
        final Size size = view.getMaximumSize();
        location.subtract(size.getWidth() / 2, size.getHeight() / 2);
        view.setLocation(location);
        setOverlayView(view);
    }

    public void setRenderingArea(final RenderingArea renderingArea) {
        this.renderingArea = renderingArea;
    }

    public void setRootView(final View rootView) {
        this.rootView = rootView;
        rootView.invalidateContent();
    }

    public void setHelpViewer(final HelpViewer helpViewer) {
        this.helpViewer = helpViewer;
    }

    public void setShowMouseSpy(final boolean showDeveloperStatus) {
        if (spy.isVisible()) {
            spy.close();
        } else {
            spy.open();
        }
    }

    public void setUpdateNotifier(final ViewUpdateNotifier updateNotifier) {
        this.updateNotifier = updateNotifier;
    }

    private void locateInCentre(final View view) {
        final Size rootSize = rootView.getSize();
        final Location location = new Location(rootSize.getWidth() / 2, rootSize.getHeight() / 2);
        final Size dialogSize = view.getRequiredSize(new Size());
        location.subtract(dialogSize.getWidth() / 2, dialogSize.getHeight() / 2);
        view.setLocation(location);
    }

    public void showSpy() {
        spy.open();
    }

    public void sizeChange() {
        initSize();
        final View subviews[] = rootView.getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            subviews[i].invalidateLayout();
        }
        final Bounds bounds = new Bounds(internalDisplaySize);
        markDamaged(bounds);
        scheduleRepaint();
    }

    public void initSize() {
        internalDisplaySize = createSize(renderingArea.getSize());
        insets = renderingArea.getInsets();
        LOG.debug("  insets " + insets);
        internalDisplaySize.contract(insets.left + insets.right, insets.top + insets.bottom);
        LOG.debug("  internal " + internalDisplaySize);
        final Size rootViewSize = new Size(internalDisplaySize);
        Text text = Toolkit.getText(ColorsAndFonts.TEXT_STATUS);
        statusBarHeight = text.getLineHeight() + text.getDescent();
        rootViewSize.contractHeight(statusBarHeight);
        statusBarArea = new Bounds(insets.left, insets.top + rootViewSize.getHeight(), rootViewSize.getWidth(), statusBarHeight);
        rootView.setSize(rootViewSize);
    }

    public void thirdClick(final Click click) {
        if (onOverlay(click.getLocation())) {
            click.subtract(overlayView.getLocation());
            overlayView.thirdClick(click);
        } else {
            rootView.thirdClick(click);
        }
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("renderingArea", renderingArea);
        str.append("redrawArea", redrawArea);
        str.append("rootView", rootView);
        return str.toString();
    }

    public void translate(final MouseEvent me) {
        me.translatePoint(-insets.left, -insets.top);
    }

    public ViewAreaType viewAreaType(final Location location) {
        if (onOverlay(location)) {
            location.subtract(overlayView.getLocation());
            return overlayView.viewAreaType(location);
        } else {
            return rootView.viewAreaType(location);
        }
    }

    public boolean isOverlayAvailable() {
        return overlayView != CLEAR_OVERLAY;
    }

    public void makeRootFocus() {
    }

    public void openHelp(final View forView) {
        if (forView != null) {
            String description = null;
            String help = null;
            String name = null;
            if (forView != null && forView.getContent() != null) {
                final Content content = forView.getContent();
                description = content.getDescription();
                help = content.getHelp();
                name = content.getId();
                name = name == null ? content.title() : name;
            }
            helpViewer.open(forView.getAbsoluteLocation(), name, description, help);
        }
    }

    public Object getClipboard(final Class<?> cls) {
        if (cls == String.class) {
            final Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
            final Transferable content = cb.getContents(this);
            String value = "illegal value";
            try {
                value = ((String) content.getTransferData(DataFlavor.stringFlavor));
            } catch (final Throwable e) {
                LOG.error("invalid clipboard operation " + e);
            }
            return value;
        } else {
            return null;
        }
    }

    public void setClipboard(final String clip, final Class<?> class1) {
        final Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(new StringSelection(clip), null);
    }

    public void forcePaintOfStatusBar() {
        status = feedbackManager.getStatusBarOutput();
        refreshStatus = true;
        scheduleRepaint();
    }

    public void showDialog(final MessageContent content) {
        ViewRequirement requirement = new ViewRequirement(content, ViewRequirement.OPEN);
        final View view = Toolkit.getViewFactory().createView(requirement);
        locateInCentre(view);
        rootView.getWorkspace().addDialog(view);
        scheduleRepaint();
    }

    public void clearAction() {
        feedbackManager.clearAction();
        clearOverlayView();
    }

    public void setFeedbackManager(final XFeedbackManager feedbackManager) {
        this.feedbackManager = feedbackManager;
    }

    public void close() {
        renderingArea.dispose();
    }
}
