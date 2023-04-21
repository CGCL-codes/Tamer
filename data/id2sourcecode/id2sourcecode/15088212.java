    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) UIUtils.getActiveEditor().getAdapter(GraphicalViewer.class);
        VisualDBInformationControl quickOutline = new VisualDBInformationControl(viewer.getControl().getShell(), viewer);
        quickOutline.setVisible(true);
    }
