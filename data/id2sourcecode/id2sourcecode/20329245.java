    private void addOverview(GraphicalViewer viewer, VerticalPanel panel) {
        org.eclipse.swt.widgets.Canvas c = new org.eclipse.swt.widgets.Canvas(false);
        LightweightSystem lws = new LightweightSystem(c);
        panel.add(c.getWidget());
        c.setSize(150, 150);
        RootEditPart rep = viewer.getRootEditPart();
        if (rep instanceof ScalableFreeformRootEditPart) {
            ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
            ScrollableThumbnailEx thumbnail = new ScrollableThumbnailEx((Viewport) root.getFigure());
            thumbnail.setBorder(new MarginBorder(3));
            thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
            lws.setContents(thumbnail);
        }
    }
