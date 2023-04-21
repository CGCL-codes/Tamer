    public void createControl(Composite parent) {
        sashForm = new SashForm(parent, SWT.VERTICAL);
        getViewer().createControl(sashForm);
        getViewer().setEditDomain(getEditDomain());
        getViewer().setEditPartFactory(new DashboardTreeEditPartFactory());
        ContextMenuProvider cmProvider = new DashboardContextMenuProvider(getViewer(), getActionRegistry());
        getViewer().setContextMenu(cmProvider);
        getSite().registerContextMenu("net.entropysoft.dashboard.plugin.dashboard.outline.contextmenu", cmProvider, getSite().getSelectionProvider());
        getSelectionSynchronizer().addViewer(getViewer());
        getViewer().setContents(dashboardEditor.getDashboard());
        ScalableRootEditPart rootEditPart = (ScalableRootEditPart) getGraphicalViewer().getRootEditPart();
        Canvas canvas = new Canvas(sashForm, SWT.BORDER);
        LightweightSystem lightweightSystem = new LightweightSystem(canvas);
        thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
        thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
        lightweightSystem.setContents(thumbnail);
    }
