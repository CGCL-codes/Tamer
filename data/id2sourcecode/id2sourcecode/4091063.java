    @Override
    public Object getAdapter(Class type) {
        if (type == GraphicalViewer.class) return viewer;
        if (type == CommandStack.class) return editDomain.getCommandStack();
        if (type == EditPart.class && viewer != null) return viewer.getRootEditPart();
        if (type == IFigure.class && viewer != null) return ((GraphicalEditPart) viewer.getRootEditPart()).getFigure();
        if (type == RecorderEditPart.class && viewer != null) return viewer.getContents();
        return super.getAdapter(type);
    }
