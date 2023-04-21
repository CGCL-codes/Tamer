    public ICommand getCommand(IGlobalActionContext cntxt) {
        IWorkbenchPart part = cntxt.getActivePart();
        if (!(part instanceof IDiagramWorkbenchPart)) {
            return null;
        }
        IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) part;
        ICommand command = null;
        String actionId = cntxt.getActionId();
        if (actionId.equals(GlobalActionId.DELETE)) {
            super.getCommand(cntxt);
        } else if (actionId.equals(GlobalActionId.COPY)) {
            command = getCopyCommand(cntxt, diagramPart, false);
            editPartClipboard.clear();
            cutClipboard.clear();
            Object[] objects = ((IStructuredSelection) cntxt.getSelection()).toArray();
            for (Object o : objects) {
                editPartClipboard.add((EditPart) o);
            }
        } else if (actionId.equals(GlobalActionId.CUT)) {
            command = getCutCommand(cntxt, diagramPart);
            HashMap<EObject, EObject> m = new HashMap<EObject, EObject>();
            editPartClipboard.clear();
            cutClipboard.clear();
            Object[] objects2 = ((IStructuredSelection) cntxt.getSelection()).toArray();
            for (Object ep : objects2) {
                editPartClipboard.add((EditPart) ep);
                final EObject o = ((IGraphicalEditPart) ep).getNotationView().getElement();
                cutClipboard.add(o);
                m.put(o, cutClipboard.get(cutClipboard.indexOf(o)));
            }
            cutDeleted = false;
        } else if (actionId.equals(GlobalActionId.OPEN)) {
            super.getCommand(cntxt);
        } else if (actionId.equals(GlobalActionId.PASTE)) {
            PasteViewRequest pasteReq = createPasteViewRequest();
            Object[] objects = ((IStructuredSelection) cntxt.getSelection()).toArray();
            if (objects.length == 1) {
                Command paste;
                EditPart ep = (EditPart) objects[0];
                if (objects[0] instanceof ActorEditPart) {
                    ep = ((ActorEditPart) objects[0]).getChildBySemanticHint(Integer.toString(ActorActorCompartmentEditPart.VISUAL_ID));
                    paste = ep.getCommand(pasteReq);
                } else if (objects[0] instanceof AgentEditPart) {
                    ep = ((AgentEditPart) objects[0]).getChildBySemanticHint(Integer.toString(AgentAgentCompartmentEditPart.VISUAL_ID));
                    paste = ep.getCommand(pasteReq);
                } else if (objects[0] instanceof RoleEditPart) {
                    ep = ((RoleEditPart) objects[0]).getChildBySemanticHint(Integer.toString(RoleRoleCompartmentEditPart.VISUAL_ID));
                    paste = ep.getCommand(pasteReq);
                } else if (objects[0] instanceof PositionEditPart) {
                    ep = ((PositionEditPart) objects[0]).getChildBySemanticHint(Integer.toString(PositionPositionCompartmentEditPart.VISUAL_ID));
                    paste = ep.getCommand(pasteReq);
                } else {
                    paste = ((EditPart) objects[0]).getCommand(pasteReq);
                }
                if (paste != null) {
                    CommandStack cs = diagramPart.getDiagramEditDomain().getDiagramCommandStack();
                    TransactionalEditingDomain copyFromDomain = ((IGraphicalEditPart) editPartClipboard.get(0)).getEditingDomain();
                    TransactionalEditingDomain pasteToDomain = ((IGraphicalEditPart) ep).getEditingDomain();
                    EObject container = ((IGraphicalEditPart) ep).getNotationView().getElement();
                    List<CreateElementCommand> commandList = getCreateCommandList(pasteToDomain, container);
                    map.clear();
                    for (CreateElementCommand c : commandList) {
                        ICommandProxy create = new ICommandProxy(c);
                        cs.execute(create);
                        map.put(((CreateDuplicateElementCommand) c).getOriginal(), ((CreateDuplicateElementCommand) c).getDuplicate());
                    }
                    if (!cutClipboard.isEmpty() && !cutDeleted) {
                        CompositeTransactionalCommand delete = new CompositeTransactionalCommand(copyFromDomain, cntxt.getLabel());
                        for (EditPart editPart : editPartClipboard) {
                            GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
                            Command deleteCommand = editPart.getCommand(deleteReq);
                            if (deleteCommand != null) {
                                delete.compose(new CommandProxy(deleteCommand));
                            }
                        }
                        cutClipboard.clear();
                        for (EObject e : map.keySet()) {
                            cutClipboard.add(map.get(e));
                        }
                        cutDeleted = true;
                        return delete;
                    }
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
        } else {
            System.out.println("actionID is : " + actionId);
        }
        return command;
    }
