                        public void mouseDoubleClick(MouseEvent e) {
                            Tool tool = getPaletteViewer().getActiveTool().createTool();
                            if (tool instanceof CreationTool || tool instanceof ConnectionCreationTool) {
                                tool.setViewer(getDiagramGraphicalViewer());
                                tool.setEditDomain(getDiagramGraphicalViewer().getEditDomain());
                                tool.mouseDoubleClick(e, getDiagramGraphicalViewer());
                                clearActiveTool = true;
                            }
                        }
