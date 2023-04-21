    public void propertyChange(PropertyChangeEvent evt) {
        applyPreferences();
        RootEditPart root = (RootEditPart) getGraphicalViewer().getRootEditPart().getContents();
        root.propertyChange(new java.beans.PropertyChangeEvent(this, RootModel.P_MODE, null, null));
    }
