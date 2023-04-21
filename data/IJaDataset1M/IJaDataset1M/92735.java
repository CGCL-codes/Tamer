package com.jiexplorer.ui;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class TreeViewer extends JTree {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7081896507028468884L;

    public TreeViewer() {
        super();
    }

    public TreeViewer(Hashtable<?, ?> value) {
        super(value);
    }

    public TreeViewer(Object[] value) {
        super(value);
    }

    public TreeViewer(TreeModel newModel) {
        super(newModel);
    }

    public TreeViewer(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

    public TreeViewer(TreeNode root) {
        super(root);
    }

    public TreeViewer(Vector<?> value) {
        super(value);
    }

    protected JPopupMenu popup;

    protected JMenuItem jMenuItemExpand;

    protected JMenuItem jMenuItemRefresh;

    protected JMenuItem jMenuItemCopy;

    protected JMenuItem jMenuItemDelete;

    protected JMenuItem jMenuItemRename;

    protected JMenuItem jMenuItemNewFolder;

    protected JMenuItem startThumbnailScan;

    protected JMenuItem stopThumbnailScan;
}
