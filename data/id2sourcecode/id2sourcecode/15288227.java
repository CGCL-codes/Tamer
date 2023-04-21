        public void createControl(Composite parent) {
            this.canvas = new Canvas(parent, SWT.BORDER);
            LightweightSystem lws = new LightweightSystem(canvas);
            ScalableRootEditPart rootEditPart = (ScalableRootEditPart) getGraphicalViewer().getRootEditPart();
            this.thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
            this.thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
            lws.setContents(thumbnail);
            disposeListener = new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (thumbnail != null) {
                        thumbnail.deactivate();
                        thumbnail = null;
                    }
                }
            };
            getGraphicalViewer().getControl().addDisposeListener(disposeListener);
        }
