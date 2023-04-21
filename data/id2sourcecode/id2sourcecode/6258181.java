    public VisualDBInformationControl(Shell parentShell, GraphicalViewer graphicalViewer) {
        super(parentShell, true);
        this.graphicalViewer = graphicalViewer;
        this.modelEditor = new ModelEditor(graphicalViewer, false);
        VisualDBOutlinePage.setFilterText("");
        create();
        int width = 300;
        int height = 300;
        Point loc = graphicalViewer.getControl().getParent().toDisplay(0, 0);
        Point size = graphicalViewer.getControl().getParent().getSize();
        int x = (size.x - width) / 2 + loc.x;
        int y = (size.y - height) / 2 + loc.y;
        setSize(width, height);
        setLocation(new Point(x, y));
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                dispose();
            }
        });
    }
