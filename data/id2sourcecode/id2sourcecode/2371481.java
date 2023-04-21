    @Override
    public void createPartControl(Composite parent) {
        basePartName = getPartName();
        canvas = new Canvas(parent, SWT.NONE);
        viewer = new GraphicalViewer(canvas);
        installResourceListener();
        installSelectionListener();
        installPartListener();
    }
