    @Override
    protected void showDiagram() {
        if (myVisualizer == null) {
            assert getEditorInput() instanceof HierarchyEditorInput;
            myModelProvider = new HierarchyModelProvider();
            myModelProvider.addModelChangeListener(this);
            myRefreshPerformer = new RefreshPerformer(this);
            MenuManager contextMenu = buildContextMenu();
            DiagramVisualizerSettings settings = new DiagramVisualizerSettings();
            settings.setEditPartFactory(new HierarchyEditPartFactory());
            settings.setModelProvider(myModelProvider);
            settings.setLayoutPerformer(new HierarchyLayoutPerformer());
            settings.setInfiniteDiagramSize(true);
            settings.setContentMenu(contextMenu);
            ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
            ConnectionLayer connectionLayer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
            connectionLayer.setConnectionRouter(new PointsBasedConnectionRouter());
            settings.setRootEditPart(rootEditPart);
            settings.setToolbarBuilder(new ToolbarBuilder() {

                public Iterable<Iterable<IAction>> buildToolbar(GraphicalViewer graphicalViewer) {
                    ArrayList<Iterable<IAction>> toolbar = new ArrayList<Iterable<IAction>>();
                    ArrayList<IAction> navigateActions = new ArrayList<IAction>();
                    navigateActions.add(new RefreshAction(myRefreshPerformer));
                    navigateActions.add(new ExpandAction(myModelProvider));
                    navigateActions.add(new CollapseAction(myModelProvider));
                    toolbar.add(navigateActions);
                    ArrayList<IAction> zoomActions = new ArrayList<IAction>();
                    zoomActions.add(new ZoomInAction(HierarchyEditor.this, graphicalViewer));
                    zoomActions.add(new ZoomOutAction(HierarchyEditor.this, graphicalViewer));
                    zoomActions.add(new ZoomFitAction(HierarchyEditor.this, graphicalViewer));
                    zoomActions.add(new ZoomOriginalAction(HierarchyEditor.this, graphicalViewer));
                    toolbar.add(zoomActions);
                    ArrayList<IAction> filterActions = new ArrayList<IAction>();
                    filterActions.add(new FilterAction(HierarchyEditor.this, FILTER_ITEMS));
                    toolbar.add(filterActions);
                    ArrayList<IAction> printActions = new ArrayList<IAction>();
                    printActions.add(new PrintAction(HierarchyEditor.this, graphicalViewer));
                    toolbar.add(printActions);
                    return toolbar;
                }
            });
            myVisualizer = new DiagramVisualizer(settings, myDiagramPane);
            myVisualizer.getControl().setBackground(ColorConstants.white);
            getSite().registerContextMenu(contextMenu, myVisualizer.getSelectionProvider());
            mySelectionProvider.setDelegate(myVisualizer.getSelectionProvider());
            myInplaceManager = new InplaceManagerImpl();
            myInplaceManager.setVisualizer(myVisualizer);
            myModelProvider.setInput(((HierarchyEditorInput) getEditorInput()).getInputElementWrapper(), true);
            expandTasks(false);
            expandTasks(true);
        }
        myPageBook.showPage(myDiagramPane);
    }
