    @Override
    protected boolean calculateEnabled() {
        GraphicalViewer viewer = this.getGraphicalViewer();
        for (Object object : viewer.getSelectedEditParts()) {
            if (object instanceof NodeElementEditPart) {
                NodeElementEditPart nodeElementEditPart = (NodeElementEditPart) object;
                if (nodeElementEditPart instanceof ERTableEditPart || nodeElementEditPart instanceof NoteEditPart) {
                    return true;
                }
            }
        }
        return false;
    }
