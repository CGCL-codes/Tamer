        protected void initializeOverview() {
            LightweightSystem lightweightsystem = new LightweightSystem(overview);
            org.eclipse.gef.RootEditPart rooteditpart = getGraphicalViewer().getRootEditPart();
            if (rooteditpart instanceof ScalableFreeformRootEditPart) {
                ScalableFreeformRootEditPart scalablefreeformrooteditpart = (ScalableFreeformRootEditPart) rooteditpart;
                thumbnail = new ScrollableThumbnail((Viewport) scalablefreeformrooteditpart.getFigure());
                thumbnail.setBorder(new MarginBorder(3));
                thumbnail.setSource(scalablefreeformrooteditpart.getLayer("Printable Layers"));
                lightweightsystem.setContents(thumbnail);
                disposeListener = new DisposeListener() {

                    public void widgetDisposed(DisposeEvent disposeevent) {
                        if (thumbnail != null) {
                            thumbnail.deactivate();
                            thumbnail = null;
                        }
                    }
                };
                getEditor().addDisposeListener(disposeListener);
            }
        }
