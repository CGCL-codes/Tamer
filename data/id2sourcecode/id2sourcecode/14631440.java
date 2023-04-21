    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitAbstractNavigatorItem) {
            return;
        }
        de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitAbstractNavigatorItem abstractNavigatorItem = (de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorItem) {
            navigatorView = ((de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorGroup) {
            de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorGroup navigatorGroup = (de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorItem) {
                navigatorView = ((de.hu_berlin.sam.mmunit.diagram.navigator.MMUnitNavigatorItem) navigatorGroup.getParent()).getView();
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
