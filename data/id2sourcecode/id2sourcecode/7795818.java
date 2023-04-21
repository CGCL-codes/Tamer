    protected void initializeGraphicalViewer() {
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        getGraphicalViewer().setRootEditPart(rootEditPart);
        getGraphicalViewer().setContents(dashboard);
        updateRulers();
        getGraphicalViewer().addDropTargetListener(new JmxAttributeDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener(new FileImageDropTargetListener(this, getGraphicalViewer()));
    }
