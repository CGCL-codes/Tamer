            public void actualiseEditeursChaines(Chain oldChain) {
                try {
                    List<PObjectReference> chaineAMAJ = Application.getCurrentChainRepository().getObjectRegisteredConsumers(oldChain.getId(), oldChain.getVersion(), false);
                    if (chaineAMAJ.size() > 0) {
                        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        IEditorReference[] tabEditorsRefs = page.getEditorReferences();
                        for (IEditorReference refEditor : tabEditorsRefs) {
                            IEditorPart editor = refEditor.getEditor(false);
                            if (editor instanceof ChainEditor) {
                                Chain c = ((ChainEditor) editor).getChain();
                                for (PObjectReference ref : chaineAMAJ) {
                                    Chain dep = ref.resolve(Application.getCurrentChainRepository(), false, false);
                                    if (dep != null && dep.getId().equals(c.getId()) && dep.getVersion().equals(c.getVersion())) {
                                        System.out.println("Cet éditeur " + editor.getTitle() + " utilise notre chaine");
                                        ((ChainEditor) editor).initializeGraphicalViewer();
                                    }
                                }
                            }
                        }
                    }
                } catch (PObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
