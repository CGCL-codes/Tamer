    @SuppressWarnings("unchecked")
    public Object getAdapter(Class type) {
        if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class) {
            PropertySheetPage page = new PropertySheetPage();
            page.setRootEntry(new UndoablePropertySheetEntry(getCommandStack()));
            return page;
        }
        if (type == GraphicalViewer.class) return getGraphicalViewer();
        if (type == CommandStack.class) return getCommandStack();
        if (type == ActionRegistry.class) return getActionRegistry();
        if (type == EditPart.class && getGraphicalViewer() != null) return getGraphicalViewer().getRootEditPart();
        if (type == IFigure.class && getGraphicalViewer() != null) return ((GraphicalEditPart) getGraphicalViewer().getRootEditPart()).getFigure();
        return super.getAdapter(type);
    }
