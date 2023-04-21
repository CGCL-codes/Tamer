    public OverviewDialog(GraphicalViewer viewer) {
        super(viewer.getControl().getShell());
        myGraphicalViewer = viewer;
        setShellStyle(SWT.RESIZE);
        myOverviewVisible = false;
        myFocusAdapter = new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                mouseUp(null);
            }
        };
    }
