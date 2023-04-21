    private GraphicalViewer getGraphicalViewer() {
        try {
            ShapesEditor se = (ShapesEditor) window.getActivePage().getActiveEditor();
            return se.getGraphicalViewer();
        } catch (NullPointerException npe) {
            return null;
        }
    }
