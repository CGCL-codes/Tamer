    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(final Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return _viewer; else if (adapter == CommandStack.class) return getCommandStack(); else if (adapter == EditDomain.class) return getEditDomain(); else if (adapter == IPropertySheetPage.class) return getPropertySheetPage();
        return super.getAdapter(adapter);
    }
