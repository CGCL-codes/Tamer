        private void initializeOverview() {
            LightweightSystem lws = new LightweightSystem(overview);
            RootEditPart rep = fDesignViewer.getGraphicalViewer().getRootEditPart();
            if (rep instanceof GraphicalBPELRootEditPart) {
                GraphicalBPELRootEditPart root = (GraphicalBPELRootEditPart) rep;
                thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
                thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
                lws.setContents(thumbnail);
            }
        }
