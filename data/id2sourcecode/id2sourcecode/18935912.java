    @Override
    public boolean performFinish() {
        selectedElement = getSelectedElement();
        CompoundCommand cc1 = new CompoundCommand("Create Subtopic and Link");
        CreateViewRequest newNodeRequest;
        if (setVariativityPage.getGroupType() == "AND") {
            newNodeRequest = CreateViewRequestFactory.getCreateShapeRequest(PlwebElementTypes.Node_2006, selectedElement.getDiagramPreferencesHint());
        } else {
            newNodeRequest = CreateViewRequestFactory.getCreateShapeRequest(PlwebElementTypes.Group_2005, selectedElement.getDiagramPreferencesHint());
        }
        Point p = selectedElement.getFigure().getBounds().getTopRight().getCopy();
        selectedElement.getFigure().translateToAbsolute(p);
        DiagramRootEditPart mapEditPart = (DiagramRootEditPart) selectedElement.getParent();
        Command createNewNodeCmd = mapEditPart.getCommand(newNodeRequest);
        IAdaptable variativityElementViewAdapter = (IAdaptable) ((List) newNodeRequest.getNewObject()).get(0);
        cc1.add(createNewNodeCmd);
        ICommand createLinkCmd = new DeferredCreateConnectionViewAndElementCommand(new CreateConnectionViewAndElementRequest(PlwebElementTypes.SourceRefElementClass_4001, ((IHintedType) PlwebElementTypes.SourceRefElementClass_4001).getSemanticHint(), selectedElement.getDiagramPreferencesHint()), new EObjectAdapter((EObject) selectedElement.getModel()), variativityElementViewAdapter, selectedElement.getViewer());
        cc1.add(new ICommandProxy(createLinkCmd));
        selectedElement.getDiagramEditDomain().getDiagramCommandStack().execute(cc1);
        final EditPartViewer selectedElementViewer = selectedElement.getViewer();
        final EditPart selectedElementPart = (EditPart) selectedElementViewer.getEditPartRegistry().get(variativityElementViewAdapter.getAdapter(View.class));
        if (selectedElementPart != null) {
            Object element = ((NodeImpl) selectedElementPart.getModel()).getElement();
            TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(element);
            specifyGroupType(selectedElementPart, element, editingDomain);
            setGroupTitle(element, editingDomain);
            setGroupParent(element, editingDomain, ((NodeImpl) selectedElement.getModel()).getElement());
            Display.getCurrent().asyncExec(new Runnable() {

                public void run() {
                    selectedElementViewer.setSelection(new StructuredSelection(selectedElementPart));
                    Request der = new Request(RequestConstants.REQ_DIRECT_EDIT);
                    selectedElementPart.performRequest(der);
                }
            });
        }
        CompoundCommand cc2 = new CompoundCommand("edit");
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) page.getActiveEditor();
        IDiagramGraphicalViewer diagramGraphicalViewer = diagramPart.getDiagramGraphicalViewer();
        for (Object childElement : setVariativityPage.getCheckedElements()) {
            List childElementEditParts = diagramGraphicalViewer.findEditPartsForElement(EMFCoreUtil.getProxyID((EObject) childElement), TargetRefElementEditPart.class);
            EditPart childEditPart = (EditPart) childElementEditParts.get(0);
            ICommand createChildLinkCmd = new DeferredCreateConnectionViewAndElementCommand(new CreateConnectionViewAndElementRequest(PlwebElementTypes.SourceRefElementClass_4001, ((IHintedType) PlwebElementTypes.SourceRefElementClass_4001).getSemanticHint(), ((GraphicalEditPart) selectedElementPart).getDiagramPreferencesHint()), variativityElementViewAdapter, new EObjectAdapter((EObject) childEditPart.getModel()), selectedElementPart.getViewer());
            cc2.add(new ICommandProxy(createChildLinkCmd));
            ICommand removeOldLink = new DestroyReferenceCommand(new DestroyReferenceRequest((EObject) selectedElement.getModel(), ((EObject) selectedElement.getModel()).eContainmentFeature(), (EObject) childElement, false));
            cc2.add(new ICommandProxy(removeOldLink));
            TransactionalEditingDomain childElementEditingDomain = TransactionUtil.getEditingDomain(childElement);
            setElementParent(childElement, childElementEditingDomain, ((NodeImpl) selectedElementPart.getModel()).getElement());
        }
        selectedElement.getDiagramEditDomain().getDiagramCommandStack().execute(cc2);
        EObject modelElement = ((View) mapEditPart.getModel()).getElement();
        List editPolicies = CanonicalEditPolicy.getRegisteredEditPolicies(modelElement);
        for (Iterator it = editPolicies.iterator(); it.hasNext(); ) {
            CanonicalEditPolicy nextEditPolicy = (CanonicalEditPolicy) it.next();
            nextEditPolicy.refresh();
        }
        IEditorPart activeEditor = page.getActiveEditor();
        IEditorInput iEditorInput = activeEditor.getEditorInput();
        page.closeEditor(activeEditor, true);
        try {
            page.openEditor(iEditorInput, PlwebDiagramEditor.ID);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return true;
    }
