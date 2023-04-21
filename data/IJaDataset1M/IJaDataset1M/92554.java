package org.plazmaforge.studio.reportdesigner.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.plazmaforge.studio.reportdesigner.ReportDesignerPlugin;
import org.plazmaforge.studio.reportdesigner.model.DesignContainer;
import org.plazmaforge.studio.reportdesigner.model.IElement;
import org.plazmaforge.studio.reportdesigner.model.IOutlineContainer;
import org.plazmaforge.studio.reportdesigner.model.ResourceFactory;
import org.plazmaforge.studio.reportdesigner.model.table.ITableCell;
import org.plazmaforge.studio.reportdesigner.parts.ReportTreePartFactory.IDeleteCommander;
import org.plazmaforge.studio.reportdesigner.parts.properties.EmptyPropertySource;
import org.plazmaforge.studio.reportdesigner.parts.properties.ReportPropertySourceProvider;
import org.plazmaforge.studio.reportdesigner.policies.ReportTreeComponentEditPolicy;

public class ReportTreeElementEditPart extends AbstractTreeEditPart implements PropertyChangeListener, IPropertySource {

    private IDeleteCommander deleteCommander;

    protected IElement getElement() {
        return (IElement) getModel();
    }

    public void activate() {
        if (isActive()) {
            return;
        }
        super.activate();
        IElement element = getElement();
        if (element == null) {
            return;
        }
        element.addPropertyChangeListener(this);
    }

    public void deactivate() {
        if (!isActive()) {
            return;
        }
        super.deactivate();
        IElement element = getElement();
        if (element == null) {
            return;
        }
        element.removePropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent event) {
        String property = event.getPropertyName();
        if (DesignContainer.CHILDREN.equals(property)) {
            refreshChildren();
        } else {
            refreshVisuals();
        }
    }

    protected List getModelChildren() {
        Object element = getElement();
        if (element == null || !(element instanceof IOutlineContainer)) {
            return Collections.EMPTY_LIST;
        }
        return ((IOutlineContainer) element).getOutlineChildren();
    }

    protected void refreshVisuals() {
        if (getWidget() instanceof Tree) {
            return;
        } else {
            IElement element = getElement();
            setWidgetText(element);
            setWidgetImage(element);
        }
    }

    private void setWidgetText(IElement element) {
        String text = getTextByElement(element);
        if (text == null) {
            return;
        }
        setWidgetText(text);
    }

    private void setWidgetImage(IElement element) {
        Image image = getImageByElement(element);
        if (image == null) {
            return;
        }
        setWidgetImage(image);
    }

    private String getTextByElement(IElement element) {
        return ResourceFactory.getLabelByElement(element);
    }

    private Image getImageByElement(IElement element) {
        return ResourceFactory.getImageByElement(element);
    }

    protected void createEditPolicies() {
        if (canDelete()) {
            installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportTreeComponentEditPolicy(deleteCommander));
        }
    }

    private boolean canDelete() {
        IElement element = getElement();
        return !(element instanceof ITableCell);
    }

    private static ImageDescriptor getImageDescriptor(String path) {
        return ReportDesignerPlugin.getImageDescriptor(path);
    }

    private IPropertySourceProvider propertySourceProvider;

    protected IPropertySourceProvider getPropertySourceProvider() {
        if (propertySourceProvider == null) {
            propertySourceProvider = new ReportPropertySourceProvider();
        }
        return propertySourceProvider;
    }

    protected IPropertySource getPropertySource() {
        IPropertySource propertySource = getPropertySourceProvider().getPropertySource(getElement());
        return propertySource == null ? new EmptyPropertySource() : propertySource;
    }

    public Object getEditableValue() {
        return getPropertySource().getEditableValue();
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        return getPropertySource().getPropertyDescriptors();
    }

    public Object getPropertyValue(Object id) {
        return getPropertySource().getPropertyValue(id);
    }

    public boolean isPropertySet(Object id) {
        return getPropertySource().isPropertySet(id);
    }

    public void resetPropertyValue(Object id) {
        getPropertySource().resetPropertyValue(id);
    }

    public void setPropertyValue(Object id, Object value) {
        getPropertySource().setPropertyValue(id, value);
    }

    public IDeleteCommander getDeleteCommander() {
        return deleteCommander;
    }

    public void setDeleteCommander(IDeleteCommander deleteCommander) {
        this.deleteCommander = deleteCommander;
    }
}
