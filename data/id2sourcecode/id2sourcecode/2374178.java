    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof dataflowScheme.diagram.navigator.ModelAbstractNavigatorItem) {
            return;
        }
        dataflowScheme.diagram.navigator.ModelAbstractNavigatorItem abstractNavigatorItem = (dataflowScheme.diagram.navigator.ModelAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof dataflowScheme.diagram.navigator.ModelNavigatorItem) {
            navigatorView = ((dataflowScheme.diagram.navigator.ModelNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof dataflowScheme.diagram.navigator.ModelNavigatorGroup) {
            dataflowScheme.diagram.navigator.ModelNavigatorGroup navigatorGroup = (dataflowScheme.diagram.navigator.ModelNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof dataflowScheme.diagram.navigator.ModelNavigatorItem) {
                navigatorView = ((dataflowScheme.diagram.navigator.ModelNavigatorItem) navigatorGroup.getParent()).getView();
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
