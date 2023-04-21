                public void run() {
                    ITypeRoot tr = sourceEditor.getInputElement();
                    try {
                        getGraphicalViewer().setContents(tr);
                        ((DesignTimeComponent) fact.getTopPart().getModelChildren().get(0)).addListener(new DesignTimeListener() {

                            public void handleUpdate(DesignTimeComponent comp) {
                                updateSourceWithComponent(comp.getTopLevel());
                            }
                        });
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
