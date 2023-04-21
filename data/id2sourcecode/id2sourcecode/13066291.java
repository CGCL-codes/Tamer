    public void changeCategory() {
        this.outlinePage.setCategory(this.getEditDomain(), this.getGraphicalViewer(), this.outlineMenuMgr, this.getActionRegistry());
        this.getSelectionSynchronizer().addViewer(this.outlinePage.getViewer());
    }
