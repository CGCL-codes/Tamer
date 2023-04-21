    public void setFocus() {
        if (getGraphicalViewer() != null && getGraphicalViewer().getControl() != null) {
            getGraphicalViewer().getControl().setFocus();
        }
    }
