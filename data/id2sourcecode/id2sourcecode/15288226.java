    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        if (type == IContentOutlinePage.class) {
            return new UMLContentOutlinePage();
        }
        if (type == RootModel.class) {
            return getGraphicalViewer().getContents().getModel();
        }
        if (type == CommandStack.class) {
            return getCommandStack();
        }
        return super.getAdapter(type);
    }
