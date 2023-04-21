    public GraphViewer(Composite comp, ViewSettings settings) {
        this.settings = settings;
        graphicalViewer = new ScrollingGraphicalViewer();
        graphicalViewer.createControl(comp);
        CFlowGraphicalEditor editor = new CFlowGraphicalEditor(this);
        graphicalViewer.setEditDomain(new DefaultEditDomain(editor));
        graphicalViewer.setRootEditPart(new FreeformGraphicalRootEditPart());
        graphicalViewer.setEditPartFactory(new CFlowEditPartFactory(this));
        graphicalViewer.getRootEditPart().getViewer().getControl().setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        getFigureCanvas().getViewport().addPropertyChangeListener(Viewport.PROPERTY_VIEW_LOCATION, this);
    }
