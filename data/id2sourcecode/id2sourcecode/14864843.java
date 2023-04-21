    @Override
    public ICommand getCommand(IGlobalActionContext cntxt) {
        if (true) return super.getCommand(cntxt);
        IWorkbenchPart part = cntxt.getActivePart();
        if (!(part instanceof IDiagramWorkbenchPart)) {
            return null;
        }
        IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) part;
        ICommand command = null;
        String actionId = cntxt.getActionId();
        if (actionId.equals(GlobalActionId.PASTE)) {
            PasteViewRequest pasteReq = createPasteViewRequest();
            ICustomData[] data = pasteReq.getData();
            if (data != null && data.length > 0) {
                List allViews = new ArrayList();
                for (ICustomData element : data) {
                    String xml = new String(element.getData());
                    XMIResourceImpl xmiResource = new XMIResourceImpl();
                    try {
                        xmiResource.load(new ByteArrayInputStream(xml.getBytes()), null);
                        final List obs = xmiResource.getContents();
                        if (obs == null) continue;
                        HandlerEditPart handler = (HandlerEditPart) ((AsteriskRootEditPart) ((AsteriskDiagramEditor) diagramPart).getDiagramGraphicalViewer().getRootEditPart()).getContents();
                        final Diagram d = diagramPart.getDiagram();
                        final EStructuralFeature childrenFeature = d.eClass().getEStructuralFeature("children");
                        AbstractTransactionalCommand cmd = new AbstractTransactionalCommand(getEditingDomain(diagramPart), "paster", null) {

                            @Override
                            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                                for (Object o : obs) {
                                    if (o instanceof Node) {
                                        ClipboardSupportUtil.appendEObjectAt(d, (EReference) childrenFeature, (EObject) o);
                                    }
                                }
                                return CommandResult.newOKCommandResult(obs);
                            }
                        };
                        return cmd;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return super.getCommand(cntxt);
    }
