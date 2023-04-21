    public static EditDomain getEditDomain(GraphicalEditor editor) {
        GraphicalViewer viewer = getViewer(editor);
        if (viewer != null) return viewer.getEditDomain();
        return null;
    }
