    public void actualiseEditeursChaines(Chain oldChain) {
        try {
            List<PObjectReference> chaineAMAJ = chainRepository.getObjectRegisteredConsumers(oldChain.getId(), oldChain.getVersion(), false);
            if (chaineAMAJ.size() > 0) {
                IWorkbenchPage page = getEditorSite().getPage();
                IEditorReference[] tabEditorsRefs = page.getEditorReferences();
                for (IEditorReference refEditor : tabEditorsRefs) {
                    IEditorPart editor = refEditor.getEditor(false);
                    if (editor instanceof ChainEditor) {
                        Chain c = ((ChainEditor) editor).getChain();
                        for (PObjectReference ref : chaineAMAJ) {
                            Chain dep = ref.resolve(chainRepository, false, false);
                            if (dep.getId().equals(c.getId()) && dep.getVersion().equals(c.getVersion())) {
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
