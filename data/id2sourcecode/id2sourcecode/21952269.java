    @SuppressWarnings("unchecked")
    public Object getAdapter(Class type) {
        if (type == IPropertySheetPage.class) {
            PropertySheetPage page = new PropertySheetPage();
            page.setRootEntry(new UndoablePropertySheetEntry(getEditDomain().getCommandStack()));
            return page;
        }
        if (type == IContentOutlinePage.class) {
            return new BpmoContentOutlinePage(getActionRegistry(), BpmoEditor.this);
        }
        if (type == GraphicalViewer.class) {
            return getGraphicalViewer();
        }
        if (type == EditPart.class) {
            return getGraphicalViewer().getRootEditPart();
        }
        if (type == IFigure.class) {
            return ((GraphicalEditPart) getGraphicalViewer().getRootEditPart()).getFigure();
        }
        if (type == ZoomManager.class) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }
        if (type == ActionRegistry.class) {
            return getActionRegistry();
        }
        if (type == SelectionSynchronizer.class) {
            return this.synchronizer;
        }
        return super.getAdapter(type);
    }
