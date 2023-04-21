    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return getGraphicalViewer(); else if (adapter == CommandStack.class) return getCommandStack(); else if (adapter == EditDomain.class) return getEditDomain(); else if (adapter == ActionRegistry.class) return getActionRegistry(); else if (adapter == IPropertySheetPage.class) return getPropertySheetPage(); else if (adapter == IContentOutlinePage.class) return getOverviewOutlinePage(); else if (adapter == ZoomManager.class) return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        return super.getAdapter(adapter);
    }
