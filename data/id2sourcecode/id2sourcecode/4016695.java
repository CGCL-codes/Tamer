    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof org.codescale.eDependency.diagram.navigator.EDependencyAbstractNavigatorItem) {
            return;
        }
        org.codescale.eDependency.diagram.navigator.EDependencyAbstractNavigatorItem abstractNavigatorItem = (org.codescale.eDependency.diagram.navigator.EDependencyAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem) {
            navigatorView = ((org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup) {
            org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup navigatorGroup = (org.codescale.eDependency.diagram.navigator.EDependencyNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem) {
                navigatorView = ((org.codescale.eDependency.diagram.navigator.EDependencyNavigatorItem) navigatorGroup.getParent()).getView();
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
