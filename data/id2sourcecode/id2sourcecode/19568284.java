    public static GraphicalViewer getViewer(GraphicalEditor editor) {
        return (GraphicalViewer) editor.getAdapter(GraphicalViewer.class);
    }
