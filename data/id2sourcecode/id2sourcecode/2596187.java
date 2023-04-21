        protected void initializeOverview() {
            LightweightSystem lws = new LightweightSystem(overview);
            RootEditPart rep = getGraphicalViewer().getRootEditPart();
            DiagramRootEditPart root = (DiagramRootEditPart) rep;
            thumbnail = new SafiScrollableThumbnail((Viewport) root.getFigure());
            thumbnail.setSource(root.getLayer(LayerConstants.SCALABLE_LAYERS));
            lws.setContents(thumbnail);
            disposeListener = new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (thumbnail != null) {
                        thumbnail.deactivate();
                        thumbnail = null;
                    }
                }
            };
            getEditor().addDisposeListener(disposeListener);
            this.overviewInitialized = true;
        }
