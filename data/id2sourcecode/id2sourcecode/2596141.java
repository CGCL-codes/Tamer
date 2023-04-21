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
