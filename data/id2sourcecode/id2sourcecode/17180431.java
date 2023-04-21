    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramAbstractNavigatorItem) {
            return;
        }
        com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramAbstractNavigatorItem abstractNavigatorItem = (com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem) {
            navigatorView = ((com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorGroup) {
            com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorGroup navigatorGroup = (com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem) {
                navigatorView = ((com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem) navigatorGroup.getParent()).getView();
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
