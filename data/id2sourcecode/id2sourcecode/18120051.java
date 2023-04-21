    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return mainFrame.getGraphicalViewer();
        if (adapter == CommandStack.class) return getCommandStack();
        if (adapter == EditDomain.class) return getEditDomain();
        if (adapter == ActionRegistry.class) return getActionRegistry();
        return super.getAdapter(adapter);
    }
