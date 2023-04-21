    @Override
    protected boolean calculateEnabled() {
        GraphicalViewer viewer = this.getGraphicalViewer();
        if (viewer.getSelectedEditParts().isEmpty()) {
            return false;
        }
        return true;
    }
