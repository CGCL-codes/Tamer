    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        ContentsModel parent = new ContentsModel();
        HelloModel child;
        child = new HelloModel();
        child.setConstraint(new Rectangle(30, 30, -1, -1));
        parent.addChild(child);
        child = new HelloModel();
        child.setConstraint(new Rectangle(0, 0, -1, -1));
        parent.addChild(child);
        child = new HelloModel();
        child.setConstraint(new Rectangle(10, 80, 80, 50));
        parent.addChild(child);
        viewer.setContents(parent);
    }
