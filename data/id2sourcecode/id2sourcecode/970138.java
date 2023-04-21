    @Override
    public void createControl(Composite parent) {
        EditDomain editDomain = (EditDomain) this.editor.getAdapter(EditDomain.class);
        this.treeViewer.setEditDomain(editDomain);
        SelectionSynchronizer synchronizer = (SelectionSynchronizer) this.editor.getAdapter(SelectionSynchronizer.class);
        synchronizer.addViewer(this.treeViewer);
        canvas = new Canvas(parent, SWT.BORDER);
        LightweightSystem lws = new LightweightSystem(canvas);
        this.treeViewer.createControl(canvas);
        GraphicalViewer viewer = (GraphicalViewer) this.editor.getAdapter(GraphicalViewer.class);
        ScalableRootEditPart root = (ScalableRootEditPart) viewer.getRootEditPart();
        this.thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
        this.thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
        lws.setContents(this.thumbnail);
    }
