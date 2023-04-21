    private Command createCommand(List<?> selection) {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        CompoundCommand result = new CompoundCommand(TEXT);
        for (Object object : selection) {
            if (object instanceof GraphicalEditPart) {
                GraphicalEditPart editPart = (GraphicalEditPart) object;
                Object model = editPart.getModel();
                if (viewer != editPart.getViewer()) {
                    System.err.println("Wrong selection for Viewer in " + getClass());
                }
                if (model instanceof ILockable && ((ILockable) model).isLocked()) {
                    continue;
                }
                if (model instanceof IDiagramModelObject) {
                    IDiagramModelObject diagramObject = (IDiagramModelObject) model;
                    IDiagramModelContainer parent = (IDiagramModelContainer) diagramObject.eContainer();
                    if (parent == null) {
                        continue;
                    }
                    int originalPos = parent.getChildren().indexOf(diagramObject);
                    if (originalPos > 0) {
                        result.add(new SendBackwardCommand(parent, originalPos));
                    }
                }
            }
        }
        return result.unwrap();
    }
