    @Override
    protected PaletteViewerProvider createPaletteViewerProvider() {
        currentPaletteRoot = new WeakReference<PaletteRoot>(createPaletteRoot(null));
        getEditDomain().setPaletteRoot(currentPaletteRoot.get());
        return new PaletteViewerProvider(getEditDomain()) {

            @Override
            public PaletteViewer createPaletteViewer(Composite parent) {
                PaletteViewer pViewer = new PaletteViewer();
                PaletteViewerPreferences prefs = pViewer.getPaletteViewerPreferences();
                prefs.setAutoCollapseSetting(PaletteViewerPreferences.COLLAPSE_ALWAYS);
                prefs.setLayoutSetting(PaletteViewerPreferences.LAYOUT_COLUMNS);
                pViewer.createControl(parent);
                configurePaletteViewer(pViewer);
                hookPaletteViewer(pViewer);
                return pViewer;
            }

            @Override
            protected void hookPaletteViewer(PaletteViewer viewer) {
                super.hookPaletteViewer(viewer);
            }

            /**
       * Override to provide the additional behavior for the tools. Will intialize with a
       * PaletteEditPartFactory that has a TrackDragger that understand how to handle the
       * mouseDoubleClick event for shape creation tools. Also will initialize the palette
       * with a defaultTool that is the SelectToolEx that undestands how to handle the
       * enter key which will result in the creation of the shape also.
       */
            @Override
            protected void configurePaletteViewer(PaletteViewer viewer) {
                super.configurePaletteViewer(viewer);
                viewer.getKeyHandler().setParent(getPaletteKeyHandler());
                viewer.getControl().addMouseListener(getPaletteMouseListener());
                viewer.addDragSourceListener(new PaletteToolTransferDragSourceListener(viewer));
            }

            /** Key Saflet for the palette */
            KeyHandler paletteKeyHandler = null;

            /** Mouse listener for the palette */
            MouseListener paletteMouseListener = null;

            /**
       * @return Palette Key Saflet for the palette
       */
            private KeyHandler getPaletteKeyHandler() {
                if (paletteKeyHandler == null) {
                    paletteKeyHandler = new KeyHandler() {

                        /**
             * Processes a <i>key released </i> event. This method is called by the Tool
             * whenever a key is released, and the Tool is in the proper state. Override
             * to support pressing the enter key to create a shape or connection (between
             * two selected shapes)
             * 
             * @param event
             *          the KeyEvent
             * @return <code>true</code> if KeyEvent was handled in some way
             */
                        @Override
                        public boolean keyReleased(KeyEvent event) {
                            if (event.keyCode == SWT.Selection) {
                                Tool tool = getPaletteViewer().getActiveTool().createTool();
                                if (tool instanceof CreationTool || tool instanceof ConnectionCreationTool) {
                                    tool.keyUp(event, getDiagramGraphicalViewer());
                                    getPaletteViewer().setActiveTool(null);
                                    return true;
                                }
                            }
                            return super.keyReleased(event);
                        }
                    };
                }
                return paletteKeyHandler;
            }

            /**
       * @return Palette Mouse listener for the palette
       */
            private MouseListener getPaletteMouseListener() {
                if (paletteMouseListener == null) {
                    paletteMouseListener = new MouseListener() {

                        /**
             * Flag to indicate that the current active tool should be cleared after a
             * mouse double-click event.
             */
                        private boolean clearActiveTool = false;

                        /**
             * Override to support double-clicking a palette tool entry to create a shape
             * or connection (between two selected shapes).
             * 
             * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
             */
                        public void mouseDoubleClick(MouseEvent e) {
                            Tool tool = getPaletteViewer().getActiveTool().createTool();
                            if (tool instanceof CreationTool || tool instanceof ConnectionCreationTool) {
                                tool.setViewer(getDiagramGraphicalViewer());
                                tool.setEditDomain(getDiagramGraphicalViewer().getEditDomain());
                                tool.mouseDoubleClick(e, getDiagramGraphicalViewer());
                                clearActiveTool = true;
                            }
                        }

                        public void mouseDown(MouseEvent e) {
                        }

                        public void mouseUp(MouseEvent e) {
                            if (clearActiveTool) {
                                getPaletteViewer().setActiveTool(null);
                                clearActiveTool = false;
                            }
                        }
                    };
                }
                return paletteMouseListener;
            }
        };
    }
