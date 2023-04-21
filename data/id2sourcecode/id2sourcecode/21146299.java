    private Command createCommand(List<?> selection) {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        CompoundCommand result = new CompoundCommand(Messages.BringForwardAction_0);
        for (Object object : selection) {
            if (object instanceof GraphicalEditPart) {
                GraphicalEditPart editPart = (GraphicalEditPart) object;
                Object model = editPart.getModel();
                if (viewer != editPart.getViewer()) {
                    System.err.println("Wrong selection for viewer in " + getClass());
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
                    List<IDiagramModelObject> modelChildren = parent.getChildren();
                    int originalPos = modelChildren.indexOf(diagramObject);
                    if (originalPos < modelChildren.size() - 1) {
                        result.add(new BringForwardCommand(parent, originalPos));
                    }
                }
            }
        }
        return result.unwrap();
    }
