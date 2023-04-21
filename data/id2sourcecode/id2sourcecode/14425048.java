    public void run() {
        ScrollingGraphicalViewer viewer = main.getViewer();
        Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();
        while (selectedObjects.hasNext()) {
            EditPart childEditPart = (EditPart) selectedObjects.next();
            if (childEditPart instanceof ChsCompoundEditPart) {
                RemoveCompoundCommand command = new RemoveCompoundCommand();
                command.setCompound((CompoundModel) childEditPart.getModel());
                command.execute();
            }
        }
    }
