    public ICommand getCommand(IGlobalActionContext cntxt) {
        IWorkbenchPart part = cntxt.getActivePart();
        if (!(part instanceof IDiagramWorkbenchPart)) {
            return null;
        }
        IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) part;
        ICommand command = null;
        String actionId = cntxt.getActionId();
        if (actionId.equals(GlobalActionId.DELETE)) {
            CompoundCommand deleteCC = getDeleteCommand(diagramPart, cntxt);
            if (deleteCC != null && deleteCC.canExecute()) command = new CommandProxy(deleteCC);
        } else if (actionId.equals(GlobalActionId.COPY)) {
            command = getCopyCommand(cntxt, diagramPart, false);
        } else if (actionId.equals(GlobalActionId.CUT)) {
            command = getCutCommand(cntxt, diagramPart);
        } else if (actionId.equals(GlobalActionId.OPEN)) {
            command = openCommand;
        } else if (actionId.equals(GlobalActionId.PASTE)) {
            PasteViewRequest pasteReq = createPasteViewRequest();
            Object[] objects = ((IStructuredSelection) cntxt.getSelection()).toArray();
            if (objects.length == 1) {
                Command paste = ((EditPart) objects[0]).getCommand(pasteReq);
                if (paste != null) {
                    CommandStack cs = diagramPart.getDiagramEditDomain().getDiagramCommandStack();
                    cs.execute(paste);
                    diagramPart.getDiagramEditPart().getFigure().invalidate();
                    diagramPart.getDiagramEditPart().getFigure().validate();
                    selectAddedObject(diagramPart.getDiagramGraphicalViewer(), DiagramCommandStack.getReturnValues(paste));
                    return null;
                }
            }
        } else if (actionId.equals(GlobalActionId.SAVE)) {
            part.getSite().getPage().saveEditor((IEditorPart) diagramPart, false);
        } else if (actionId.equals(GlobalActionId.PROPERTIES)) {
            new PropertyPageViewAction().run();
        }
        return command;
    }
