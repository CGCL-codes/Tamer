        public void createControl(Composite parent) {
            sash = new SashForm((org.eclipse.swt.widgets.Composite) parent, SWT.VERTICAL);
            getViewer().createControl(sash);
            getViewer().setEditDomain(getEditDomain());
            getSelectionSynchronizer().addViewer(getViewer());
            Canvas canvas = new Canvas(sash, SWT.BORDER);
            LightweightSystem lws = new LightweightSystem(canvas);
            thumbnail = new ScrollableThumbnail((Viewport) ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getFigure());
            thumbnail.setSource(((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS));
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
