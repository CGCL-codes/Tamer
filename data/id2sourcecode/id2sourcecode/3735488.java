    public VisualDBOutlinePage(GraphicalViewer viewer, EditDomain domain, RootModel root, SelectionSynchronizer selectionSynchronizer) {
        super(new TreeViewer());
        this.graphicalViewer = viewer;
        this.editDomain = domain;
        this.rootModel = root;
        this.selectionSynchronizer = selectionSynchronizer;
        this.modelEditor = new ModelEditor(graphicalViewer, true);
    }
