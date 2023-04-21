    public Object getAdapter(Class type) {
        if (type == IContentOutlinePage.class) return getContentOutlinePage(); else if (type == IPropertySheetPage.class) return getPropertySheetPage(); else if (type == CommandStack.class) return getCommandStack(); else if (type == EditDomain.class) return getEditDomain(); else if (type == ActionRegistry.class) return getActionRegistry(); else if (type == GraphicalViewer.class || type == EditPartViewer.class) return getGraphicalViewer();
        return super.getAdapter(type);
    }
