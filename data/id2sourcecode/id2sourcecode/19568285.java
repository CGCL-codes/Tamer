    public static RootEditPart getRootEditPart(GraphicalEditor editor) {
        GraphicalViewer viewer = getViewer(editor);
        if (viewer != null) return viewer.getRootEditPart();
        return null;
    }
