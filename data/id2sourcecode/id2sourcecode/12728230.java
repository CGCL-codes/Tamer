    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return getGraphicalViewer();
        if (adapter == CommandStack.class) return getCommandStack();
        if (adapter == EditDomain.class) return getEditDomain();
        if (adapter == ActionRegistry.class) return getActionRegistry();
        if (adapter == IPropertySheetPage.class) return getPropertySheetPage();
        if (adapter == IContentOutlinePage.class) return getTestOverviewOutlinePage();
        if (adapter == ZoomManager.class) return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        return super.getAdapter(adapter);
    }
