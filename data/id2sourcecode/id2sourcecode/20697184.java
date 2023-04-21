        public void createControl(Composite parent) {
            sash = new SashForm((org.eclipse.swt.widgets.Composite) parent, SWT.VERTICAL);
            getViewer().createControl(sash);
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(new AppTreeEditPartFactory());
            getViewer().setContents(diagram);
            getSelectionSynchronizer().addViewer(getViewer());
            Canvas canvas = new Canvas(sash, SWT.BORDER);
            LightweightSystem lws = new LightweightSystem(canvas);
            thumbnail = new ScrollableThumbnail((Viewport) ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getFigure());
            thumbnail.setSource(((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS));
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
            IActionBars bars = getSite().getActionBars();
            ActionRegistry ar = getActionRegistry();
            bars.setGlobalActionHandler(ActionFactory.COPY.getId(), ar.getAction(ActionFactory.COPY.getId()));
            bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), ar.getAction(ActionFactory.PASTE.getId()));
        }
