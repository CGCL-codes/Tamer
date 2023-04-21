    private Command createClassDiagramClassCreateCommand() {
        CreateRequest createRequest = new CreateRequest(REQUEST_TYPE);
        createRequest.setFactory(new ClassDiagramClassFactory(classDescription));
        createRequest.setLocation(new Point(20, 20));
        GraphicalViewer graphicalViewer = ((ClassDiagramEditor) getWorkbenchPart()).getViewer();
        ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) graphicalViewer.getRootEditPart();
        if (!rootEditPart.getChildren().isEmpty() && rootEditPart.getChildren().get(0) instanceof ClassDiagramEditPart) {
            EditPart editPart = (EditPart) rootEditPart.getChildren().get(0);
            return editPart.getCommand(createRequest);
        } else {
            return UnexecutableCommand.INSTANCE;
        }
    }
