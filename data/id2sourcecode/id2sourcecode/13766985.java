    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class) return getGraphicalViewer(); else if (adapter == CommandStack.class) return this.getCommandStack(); else if (adapter == EditDomain.class) return this.getEditDomain(); else if (adapter == ActionRegistry.class) return this.actionRegistry; else if (adapter == IPropertySheetPage.class) return getPropertySheetPage(); else if (adapter == IContentOutlinePage.class) {
            return getOverviewOutlinePage();
        }
        return super.getAdapter(adapter);
    }
