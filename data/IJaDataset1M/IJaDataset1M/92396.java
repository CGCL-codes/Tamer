package de.grogra.pf.ui.tree;

import java.util.EventObject;
import javax.swing.tree.TreePath;
import de.grogra.pf.ui.*;

public class UISubTree extends de.grogra.util.SubTree implements UITree {

    protected UITree uiTree;

    public UISubTree(UITree tree, TreePath pathToRoot) {
        super(tree, pathToRoot);
        this.uiTree = tree;
    }

    public Object getParent(Object child) {
        return nodesEqual(child, pathToRoot.getLastPathComponent()) ? null : uiTree.getParent(child);
    }

    public Context getContext() {
        return uiTree.getContext();
    }

    public boolean nodesEqual(Object a, Object b) {
        return uiTree.nodesEqual(a, b);
    }

    public int getType(Object node) {
        return uiTree.getType(node);
    }

    public String getName(Object node) {
        return uiTree.getName(node);
    }

    public boolean isAvailable(Object node) {
        return uiTree.isAvailable(node);
    }

    public boolean isEnabled(Object node) {
        return uiTree.isEnabled(node);
    }

    public Object resolveLink(Object node) {
        return uiTree.resolveLink(node);
    }

    public Object getDescription(Object node, String type) {
        return uiTree.getDescription(node, type);
    }

    public void eventOccured(Object node, EventObject event) {
        uiTree.eventOccured(node, event);
    }

    public Object invoke(Object node, String method, Object arg) {
        return uiTree.invoke(node, method, arg);
    }

    public void update() {
        uiTree.update();
    }
}
