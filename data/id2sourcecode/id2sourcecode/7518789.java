    public void setCategory(EditDomain editDomain, GraphicalViewer graphicalViewer, MenuManager outlineMenuMgr, ActionRegistry registry) {
        this.graphicalViewer = graphicalViewer;
        this.viewer.setContextMenu(outlineMenuMgr);
        this.viewer.setEditDomain(editDomain);
        this.registry = registry;
        if (this.getSite() != null) {
            this.resetView(registry);
        }
    }
