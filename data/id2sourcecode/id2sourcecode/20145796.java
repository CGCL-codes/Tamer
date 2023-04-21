    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof edu.toronto.cs.openome_model.diagram.navigator.Openome_modelAbstractNavigatorItem) {
            return;
        }
        edu.toronto.cs.openome_model.diagram.navigator.Openome_modelAbstractNavigatorItem abstractNavigatorItem = (edu.toronto.cs.openome_model.diagram.navigator.Openome_modelAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorItem) {
            navigatorView = ((edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorGroup) {
            edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorGroup navigatorGroup = (edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorItem) {
                navigatorView = ((edu.toronto.cs.openome_model.diagram.navigator.Openome_modelNavigatorItem) navigatorGroup.getParent()).getView();
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
