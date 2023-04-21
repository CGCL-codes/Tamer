    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return viewer; else if (adapter == CommandStack.class) return editDomain.getCommandStack(); else if (adapter == EditDomain.class) return editDomain; else if (adapter == ActionRegistry.class) return getActionRegistry(); else if (adapter == ZoomManager.class) return getZoomManager(); else if (adapter == IContentOutlinePage.class) return new OutlinePage(this); else if (adapter == IPropertySheetPage.class) {
            if (propertySheetPage == null) propertySheetPage = new RMBenchPropertySheetPage();
            return propertySheetPage;
        }
        return super.getAdapter(adapter);
    }
