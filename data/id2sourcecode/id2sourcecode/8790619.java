    public void execute() {
        activitySupport = new ModelDiagramActivitySupport();
        activitySupport.setParent(parent.getParent());
        activitySupport.setLearningDesign(parent.getParent().getLearningDesign());
        activitySupport.setLocation(location);
        if (width > activitySupport.getMinimumWidth()) activitySupport.getSize().width = width;
        activitySupport.executeGen(false);
        parent.getParent().addChild(activitySupport);
        parent.getParent().updatePosElements();
        activitySupport.setName(name);
        Iterator it = supportetRoles.iterator();
        while (it.hasNext()) ((SupportActivity) activitySupport.getDataComponent()).addRoleRef(getRoleIdentifier(it.next()));
        ModelDiagramMain maindiag = parent.getParent();
        LearningDesignDataModel lddm = (LearningDesignDataModel) maindiag.getLearningDesign().getDataModel();
        GraphicalViewer viewer = lddm.getEditor().getGraphicalViewer();
        MainEditPart mainEditPart = (MainEditPart) viewer.getEditPartRegistry().get(maindiag);
        EditPart part = mainEditPart.findEditPartForModel(activitySupport);
        viewer.appendSelection(part);
    }
