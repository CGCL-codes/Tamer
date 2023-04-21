package net.entropysoft.jmx.plugin.jmxdashboard.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.entropysoft.jmx.plugin.Activator;
import net.entropysoft.jmx.plugin.jmxdashboard.model.propertydescriptors.ImagePathPropertyDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Image that can be put on the dashboard
 * 
 * @author cedric
 */
public class DashboardImage extends DashboardElement {

    public static final String IMAGE_PROP = "DashboardImage.Image";

    public static final String PATH_PROP = "path";

    private PathPropertyChangeListener pathPropertyChangeListener = new PathPropertyChangeListener();

    private Image image;

    public DashboardImage() {
        setWidth(50);
        setHeight(50);
        addPropertyChangeListener(pathPropertyChangeListener);
    }

    public DashboardImage(String imagePath) {
        this();
        setPath(imagePath);
    }

    public void setPath(String path) {
        setPropertyValue(PATH_PROP, path);
    }

    public void setDashboard(Dashboard dashboard) {
        super.setDashboard(dashboard);
        updateImage();
    }

    public String getPath() {
        return (String) getPropertyValue(PATH_PROP);
    }

    /**
	 * get IFile for the image
	 * 
	 * @return
	 */
    public IFile getFile() {
        IProject project = getDashboard().getProject();
        if (project == null) {
            return null;
        }
        String path = getPath();
        if (path == null) {
            return null;
        }
        return project.getFile(new Path(path));
    }

    public Image getImage() {
        return image;
    }

    private void updateImage() {
        InputStream is = null;
        try {
            IProject project = getDashboard().getProject();
            if (project == null) {
                return;
            }
            is = project.getFile(getPath()).getContents();
            image = new Image(Activator.getStandardDisplay(), is);
            setWidth(image.getBounds().width);
            setHeight(image.getBounds().height);
        } catch (Exception e) {
            image = null;
        }
        firePropertyChange(IMAGE_PROP, null, image);
    }

    public void dispose() {
        removePropertyChangeListener(pathPropertyChangeListener);
        if (image != null) {
            image.dispose();
        }
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        List list = new ArrayList(Arrays.asList(super.getPropertyDescriptors()));
        PropertyDescriptor pathPropertyDescriptor = new ImagePathPropertyDescriptor(PATH_PROP, PATH_PROP, this);
        pathPropertyDescriptor.setCategory(MISCELLANEOUS_CATEGORY);
        pathPropertyDescriptor.setDescription(Messages.DashboardImage_PathForImage);
        list.add(pathPropertyDescriptor);
        return (IPropertyDescriptor[]) list.toArray(new IPropertyDescriptor[0]);
    }

    /**
	 * Updates the image when PATH_PROP changes
	 * 
	 */
    private class PathPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (PATH_PROP.equals(evt.getPropertyName())) {
                updateImage();
            }
        }
    }
}
