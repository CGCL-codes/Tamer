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
