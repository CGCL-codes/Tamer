package gpsxml.gui.model;

import gpsxml.TagTreeNodeInterface;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class ComboBoxDataModel implements ComboBoxModel {

    private TagTreeNodeInterface treeNode = null;

    private Object selectedItem;

    private boolean showCreateNew = true;

    /** Creates a new instance of ComboBoxDataModel */
    public ComboBoxDataModel() {
    }

    public ComboBoxDataModel(boolean showCreateNew) {
        this.showCreateNew = showCreateNew;
    }

    public void setSelectedItem(Object anItem) {
        this.selectedItem = anItem;
    }

    /**
     *  
     */
    public Object getElementAt(int index) {
        return treeNode.getChild(index);
    }

    public void removeListDataListener(ListDataListener l) {
    }

    public void addListDataListener(ListDataListener l) {
    }

    public int getSize() {
        if (treeNode == null) {
            return 0;
        }
        return treeNode.getChildList().size();
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setTreeNode(TagTreeNodeInterface treeNode) {
        this.treeNode = treeNode;
        this.setSelectedItem(null);
    }
}
