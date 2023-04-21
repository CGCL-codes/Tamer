    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof MMUnitAbstractNavigatorItem) {
            return;
        }
        MMUnitAbstractNavigatorItem abstractNavigatorItem = (MMUnitAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof MMUnitNavigatorItem) {
            navigatorView = ((MMUnitNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof MMUnitNavigatorGroup) {
            MMUnitNavigatorGroup navigatorGroup = (MMUnitNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof MMUnitNavigatorItem) {
                navigatorView = ((MMUnitNavigatorItem) navigatorGroup.getParent()).getView();
            }
        }
        if (navigatorView == null) {
            return;
        }
        IEditorInput editorInput = getEditorInput(navigatorView.getDiagram());
        IEditorPart editor = aPage.findEditor(editorInput);
        if (editor == null) {
            return;
        }
        aPage.bringToTop(editor);
        if (editor instanceof DiagramEditor) {
            DiagramEditor diagramEditor = (DiagramEditor) editor;
            ResourceSet diagramEditorResourceSet = diagramEditor.getEditingDomain().getResourceSet();
            EObject selectedView = diagramEditorResourceSet.getEObject(EcoreUtil.getURI(navigatorView), true);
            if (selectedView == null) {
                return;
            }
            GraphicalViewer graphicalViewer = (GraphicalViewer) diagramEditor.getAdapter(GraphicalViewer.class);
            EditPart selectedEditPart = (EditPart) graphicalViewer.getEditPartRegistry().get(selectedView);
            if (selectedEditPart != null) {
                graphicalViewer.select(selectedEditPart);
            }
        }
    }
