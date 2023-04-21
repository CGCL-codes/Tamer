package com.ivis.xprocess.ui.editors.dynamic.elements;

import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.Xrecord;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.editors.dynamic.model.EditorContext;
import com.ivis.xprocess.ui.editors.dynamic.model.IContributeToRolledUpTitle;
import com.ivis.xprocess.ui.editors.dynamic.model.IXProcessWidget;
import com.ivis.xprocess.ui.editors.util.DirtyElementManager;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.IInternalRefreshListener;
import com.ivis.xprocess.ui.refresh.IRefreshListener;
import com.ivis.xprocess.ui.util.ComponentManager;
import com.ivis.xprocess.ui.util.FontAndColorManager;
import com.ivis.xprocess.ui.util.IListenToDispose;

public abstract class XProcessWidget implements IXProcessWidget, IRefreshListener, IInternalRefreshListener, IListenToDispose, IContributeToRolledUpTitle {

    private static final Logger logger = Logger.getLogger(XProcessWidget.class);

    protected boolean border;

    protected String id = "";

    protected String propertyName = "";

    protected String type = "";

    protected boolean readOnly;

    @Ignore(skip = true)
    protected boolean dirty = false;

    protected String context = "";

    protected String font = "";

    protected String foregroundColor = "";

    protected String backgroundColor = "";

    protected EditorContext editorContext;

    protected boolean contributeToTitle = false;

    protected String title = "";

    @Ignore(skip = true)
    protected boolean internalRefresh = false;

    private boolean needsRefreshingAfterSave = false;

    private boolean affectedByScheduler = false;

    protected boolean triggerReschedule = false;

    protected String maxTitleLines = "3";

    protected CopyOnWriteArrayList<ChangeRecord> changeRecords = new CopyOnWriteArrayList<ChangeRecord>();

    private boolean refreshesOnTransientRefresh = false;

    /**
     * @return the unique id of the xprocess widget
     */
    public String getId() {
        return id;
    }

    public void initialize(EditorContext editorContext, Composite parent) {
        this.editorContext = editorContext;
        create(parent);
        if (parent instanceof Section) {
            ((Section) parent).setClient(getControl());
        }
        setup(getControl());
        registerControl(editorContext, getControl());
        displayData();
        setupTestHarness();
    }

    protected void setupTestHarness() {
    }

    protected void setup(Control control) {
        if (control == null) {
            return;
        }
        if (this.foregroundColor.length() == 0) {
            control.setForeground(FontAndColorManager.getInstance().getEditorColor());
        } else {
            control.setForeground(FontAndColorManager.getInstance().getColor(this.foregroundColor));
        }
        if (this.backgroundColor.length() == 0) {
            control.setBackground(control.getParent().getBackground());
        } else {
            control.setBackground(FontAndColorManager.getInstance().getColor(this.backgroundColor));
        }
        if (this.font.length() == 0) {
            control.setFont(FontAndColorManager.getInstance().getEditorFont());
            control.redraw();
        } else {
            control.setFont(FontAndColorManager.getInstance().getFont(this.font));
            control.redraw();
        }
    }

    public boolean isAffectedByScheduler() {
        return affectedByScheduler;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Is the widget affected by the scheduler.
     *
     * The default is false if it is not defined in the schema.
     *
     * @param affectedByScheduler
     */
    public void setAffectedByScheduler(boolean affectedByScheduler) {
        this.affectedByScheduler = affectedByScheduler;
    }

    /**
     * Register the xprocess widget with the editor.
     *
     * @param editorContext
     * @param control
     */
    public void registerControl(EditorContext editorContext, Control control) {
        if (getId().length() != 0) {
            ComponentManager.addControl(editorContext.getContainer(), control, getId());
        }
        if (propertyName.length() != 0) {
            Xrecord record = editorContext.getRecord();
            if (hasContext()) {
                Xrecord contextObject = getContextObject(editorContext.getRecord());
                if (contextObject != null) {
                    record = contextObject;
                }
            }
            editorContext.getElementEditor().registerPropertyField(record, propertyName, this);
        } else {
            if (this instanceof XProcessInputWidget) {
                logger.error("Control - " + control + " has no property name to register itself with the Editor - " + editorContext.getElementEditor());
            }
        }
    }

    /**
     * Unregister the xprocess widget from the editor.
     *
     * @param editorContext
     */
    public void unregisterControl(EditorContext editorContext) {
        if (getId().length() != 0) {
            ComponentManager.removeControl(editorContext.getContainer(), getId());
        }
        if (propertyName.length() != 0) {
            editorContext.getElementEditor().unregisterPropertyField(editorContext.getRecord(), propertyName);
        }
    }

    public boolean isBorder() {
        return border;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Should the xprocess widget have a border.
     *
     * @param border
     */
    public void setBorder(boolean border) {
        this.border = border;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public abstract void create(Composite parent);

    public abstract Control getControl();

    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * The name of the property on the Xelement.
     *
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * When the element in the editor has become dirty call this message. It
     * registers the element against the dirty editor via the
     * DirtyElementManager.
     *
     * N.B: If the element that has changed is part of the editors element then
     * use setDirty(boolean dirty, Object object)
     *
     * @param dirty
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        if (dirty) {
            if (!editorContext.getElementEditor().isDirty()) {
                DirtyElementManager.addDirtyElement(editorContext.getElementWrapper().getElement(), editorContext.getElementEditor());
            }
            editorContext.getElementEditor().setDirty(dirty);
        }
    }

    /**
     * When a subelement in the editor has become dirty call this message
     * passing in the affected subelement. It registers the element against the
     * dirty editor via the DirtyElementManager. An example of this is when a
     * Task is edited via the Personal Planner. The Personal Planners element is
     * Person, and the affected subelement is the Task.
     *
     * N.B: If the element is the editors element then use setDirty(boolean
     * dirty)
     *
     * @param dirty
     */
    public void setDirty(boolean dirty, Object object) {
        if (editorContext.getElementEditor().getContainer().isDisposed()) {
            return;
        }
        Xelement element = null;
        if (object != null) {
            if (object instanceof Xelement) {
                element = (Xelement) object;
            }
            if (object instanceof IElementWrapper) {
                element = ((IElementWrapper) object).getElement();
            }
        }
        this.dirty = dirty;
        if (dirty) {
            if ((element != null) && !editorContext.getElementEditor().isDirty()) {
                DirtyElementManager.addDirtyElement(element, editorContext.getElementEditor());
            }
            editorContext.getElementEditor().setDirty(dirty);
        }
    }

    /**
     * @return true if the xprocess widget has been changed
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @return true if the element can be refreshed
     */
    public boolean canRefresh() {
        if (editorContext.getElementEditor().hasBeenExternallyChanged()) {
            return false;
        }
        if ((editorContext == null) || (editorContext.getElementWrapper() == null)) {
            return false;
        }
        boolean refresh = !isDirty() && !editorContext.getElementEditor().isInEditMode() && !editorContext.getElementEditor().isDirty();
        return refresh;
    }

    /**
     * Save the xprocess widgets data.
     */
    public void save() {
        setDirty(false);
    }

    protected void displayData() {
    }

    public void refreshEvent(ChangeRecord changeRecord) {
        if (changeRecord.hasChange(ChangeEventFactory.ChangeEvent.VCS_UPDATE)) {
            if (canRefresh()) {
                changeRecords.add(changeRecord);
            } else {
                setNeedsRefreshAfterSaving(true);
            }
        }
        if (changeRecord.hasChange(ChangeEventFactory.ChangeEvent.RESCHEDULED)) {
            if (this.isAffectedByScheduler()) {
                if (canRefresh()) {
                    changeRecords.add(changeRecord);
                } else {
                    setNeedsRefreshAfterSaving(true);
                }
            }
        }
    }

    public void refresh() {
        if (!changeRecords.isEmpty()) {
            displayData();
            changeRecords.clear();
        }
    }

    /**
     * The refreshEvent records all changeRecords that affect this
     * xprocess widget.
     *
     * @return true if it has recorded any changeRecords
     */
    public boolean hasChangeRecords() {
        return !changeRecords.isEmpty();
    }

    /**
     * A simple refresh that is not based in a sync or async block.
     */
    public void simpleRefresh() {
        displayData();
    }

    public void internalRefreshEvent(ChangeRecord changeRecord) {
    }

    public void dispose() {
        if (this.getControl() != null) {
            this.getControl().dispose();
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Should the widget be editable.
     *
     * @param readOnly
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getType() {
        return type;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Type can be:
     *  - reference
     *  - referenceset
     *  - recordset
     *  - xrecord
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public EditorContext getEditorContext() {
        return editorContext;
    }

    public String getText() {
        return "";
    }

    public void refreshLocalTransientExchangeElement() {
    }

    public String validate() {
        return null;
    }

    /**
     * @param needsRefreshAfterSaving
     */
    public void setNeedsRefreshAfterSaving(boolean needsRefreshAfterSaving) {
        this.needsRefreshingAfterSave = needsRefreshAfterSaving;
    }

    public boolean isTriggerReschedule() {
        return triggerReschedule;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Should a change to this widget trigger a reschedule when it is saved.
     *
     * The default is false if it is not defined in the schema.
     *
     * @param triggerReschedule
     */
    public void setTriggerReschedule(boolean triggerReschedule) {
        this.triggerReschedule = triggerReschedule;
    }

    public String getContributionToTitle() {
        return "";
    }

    public boolean isContributeToTitle() {
        return contributeToTitle;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Does the xprocess widget contribute text to its Sections
     * title.
     *
     * The default is false if it is not defined in the schema.
     *
     * @param contributeToTitle
     */
    public void setContributeToTitle(boolean contributeToTitle) {
        this.contributeToTitle = contributeToTitle;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * Often used as the text on buttons etc...
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxTitleLinesAsInt() {
        try {
            if ((maxTitleLines != null) && (maxTitleLines.length() != 0)) {
                return Integer.parseInt(maxTitleLines);
            }
        } catch (NumberFormatException numberFormatException) {
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * When wrapping the section title text how many lines to show.
     *
     * @param maxTitleLines
     */
    public void setMaxTitleLines(String maxTitleLines) {
        this.maxTitleLines = maxTitleLines;
    }

    public String getMaxTitleLines() {
        return maxTitleLines;
    }

    public void parentLayout() {
    }

    public boolean needsRefreshingAfterSave() {
        return needsRefreshingAfterSave;
    }

    public String getContext() {
        return context;
    }

    /**
     * Convert the context string into its xrecord.
     *
     * @param record
     * @return the xrecord or null
     */
    public Xrecord getContextObject(Xrecord record) {
        if ((getContext() != null) && (getContext().length() > 0)) {
            if (record.getReferenceProperty(getContext(), Xelement.class) != null) {
                Object object = record.getReferenceProperty(getContext(), Xelement.class);
                if (object instanceof Xrecord) {
                    return (Xrecord) object;
                }
            }
        }
        return null;
    }

    /**
     * Set through Jelly if defined in the XML schema.
     *
     * e.g: /Xtask.getProcess
     *
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return true if the xprocess widget has a context
     */
    public boolean hasContext() {
        if ((getContext() == null) || (getContext().length() == 0)) {
            return false;
        }
        return true;
    }

    public boolean isRefreshesOnTransientRefresh() {
        return refreshesOnTransientRefresh;
    }

    /**
     * Does the widget refresh itself when the element editor it is on also refreshes
     * its transient element?
     *
     * @param refreshesOnTransientRefresh
     */
    public void setRefreshesOnTransientRefresh(boolean refreshesOnTransientRefresh) {
        this.refreshesOnTransientRefresh = refreshesOnTransientRefresh;
    }

    public void setLayoutData(Object layoutData) {
    }
}
