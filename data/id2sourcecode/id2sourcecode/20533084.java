    public SearchDialog(Shell parentShell, GraphicalViewer viewer, ERDiagram diagram) {
        super(parentShell);
        this.setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
        this.setBlockOnOpen(false);
        this.viewer = viewer;
        this.diagram = diagram;
        this.searchManager = new SearchManager(this.diagram);
    }
