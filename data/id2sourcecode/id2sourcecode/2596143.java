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
