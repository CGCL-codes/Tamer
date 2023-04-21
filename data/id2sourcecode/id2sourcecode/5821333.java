    private GraphicalViewer getViewer(IWorkbenchPart part) {
        GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
        if (viewer != null) return viewer;
        EditPart editPart = (EditPart) part.getAdapter(EditPart.class);
        if (editPart != null) {
            EditPartViewer editPartViewer = editPart.getViewer();
            if (editPartViewer instanceof GraphicalViewer) return (GraphicalViewer) editPartViewer;
        }
        return null;
    }
