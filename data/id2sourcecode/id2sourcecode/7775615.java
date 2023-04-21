    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return getGraphicalViewer(); else if (adapter == CommandStack.class) return getCommandStack(); else if (adapter == EditDomain.class) return getEditDomain(); else if (adapter == ActionRegistry.class) return getActionRegistry(); else if (adapter == IContentOutlinePage.class) return getOverviewOutlinePage();
        return super.getAdapter(adapter);
    }
