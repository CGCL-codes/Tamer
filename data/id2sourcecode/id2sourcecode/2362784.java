    private void selectElements() {
        lddm.getEditor().getGraphicalViewer().deselectAll();
        ModelDiagramMain maindiag = lddm.getModel();
        GraphicalViewer viewer = lddm.getEditor().getGraphicalViewer();
        MainEditPart mainEditPart = (MainEditPart) viewer.getEditPartRegistry().get(maindiag);
        List toSelectElements = new ArrayList();
        toSelectElements.addAll(neueActivities.values());
        toSelectElements.addAll(this.neueSyncPoints.values());
        toSelectElements.addAll(this.neueNodePoints.values());
        Iterator it = toSelectElements.iterator();
        while (it.hasNext()) {
            ModelDiagramElement activity = (ModelDiagramElement) it.next();
            EditPart part = mainEditPart.findEditPartForModel(activity);
            viewer.appendSelection(part);
        }
    }
