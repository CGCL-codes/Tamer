    public void execute() {
        syncPoint = new ModelSyncPoint();
        syncPoint.setParent(parent.getParent());
        syncPoint.setLearningDesign(parent.getParent().getLearningDesign());
        syncPoint.setLocation(location);
        syncPoint.executeGen(false);
        parent.getParent().addChild(syncPoint);
        parent.getParent().updatePosElements();
        ModelDiagramMain maindiag = parent.getParent();
        LearningDesignDataModel lddm = (LearningDesignDataModel) maindiag.getLearningDesign().getDataModel();
        GraphicalViewer viewer = lddm.getEditor().getGraphicalViewer();
        MainEditPart mainEditPart = (MainEditPart) viewer.getEditPartRegistry().get(maindiag);
        EditPart part = mainEditPart.findEditPartForModel(this.syncPoint);
        viewer.appendSelection(part);
    }
