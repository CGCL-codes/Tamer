    public void execute() {
        activityLearning = new ModelDiagramActivityLearning();
        activityLearning.setParent(parent.getParent());
        activityLearning.setLearningDesign(parent.getParent().getLearningDesign());
        activityLearning.setLocation(location);
        if (width > activityLearning.getMinimumWidth()) activityLearning.getSize().width = width;
        activityLearning.executeGen(false);
        parent.getParent().addChild(activityLearning);
        parent.getParent().updatePosElements();
        activityLearning.setName(name);
        ModelDiagramMain maindiag = parent.getParent();
        LearningDesignDataModel lddm = (LearningDesignDataModel) maindiag.getLearningDesign().getDataModel();
        GraphicalViewer viewer = lddm.getEditor().getGraphicalViewer();
        MainEditPart mainEditPart = (MainEditPart) viewer.getEditPartRegistry().get(maindiag);
        EditPart part = mainEditPart.findEditPartForModel(activityLearning);
        viewer.appendSelection(part);
    }
