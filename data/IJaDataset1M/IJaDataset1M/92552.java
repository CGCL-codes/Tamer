package org.gvsig.symbology.gui.layerproperties;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.ILabelingMethod;

public abstract class AbstractLabelingMethodPanel extends JPanel implements PropertyChangeListener {

    public static final String PLACEMENT_CONSTRAINTS = "PLACEMENT_CONSTRAINTS";

    public static final String ALLOW_OVERLAP = "ALLOW_OVERLAP";

    public static final String ZOOM_CONSTRAINTS = "ZOOM_CONSTRAINTS";

    protected FLyrVect layer;

    protected ILabelingMethod method;

    public abstract String getName();

    public abstract Class<? extends ILabelingMethod> getLabelingMethodClass();

    public void setModel(ILabelingMethod method, FLyrVect srcLayer) throws ReadDriverException {
        if (srcLayer == null) {
            throw new ReadDriverException("Null DATASOURCE!", null);
        }
        this.layer = srcLayer;
        try {
            if (method != null && method.getClass().equals(getLabelingMethodClass())) {
                this.method = method;
            } else {
                this.method = getLabelingMethodClass().newInstance();
            }
            initializePanel();
        } catch (Exception e) {
            throw new ReadDriverException(srcLayer.getRecordset().getDriver().getName(), new Error("Unable to load labeling method. Is it in your classpath?", e));
        }
        fillPanel(this.method, srcLayer.getRecordset());
    }

    protected abstract void initializePanel();

    public abstract void fillPanel(ILabelingMethod method, SelectableDataSource dataSource) throws ReadDriverException;

    @Override
    public String toString() {
        return getName();
    }

    public ILabelingMethod getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        return getClass().equals(obj.getClass());
    }
}
