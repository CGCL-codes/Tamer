    public void run() {
        ScrollingGraphicalViewer viewer = main.getViewer();
        Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();
        while (selectedObjects.hasNext()) {
            Object model = ((EditPart) selectedObjects.next()).getModel();
            if (model instanceof GraphObject) {
                ((GraphObject) model).setHighlight(false);
            }
        }
    }
