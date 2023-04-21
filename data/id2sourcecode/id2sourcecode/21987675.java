    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorAbstractNavigatorItem) {
            return;
        }
        se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorAbstractNavigatorItem abstractNavigatorItem = (se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorItem) {
            navigatorView = ((se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorGroup) {
            se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorGroup navigatorGroup = (se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorItem) {
                navigatorView = ((se.mdh.mrtc.save.taEditor.diagram.navigator.TaEditorNavigatorItem) navigatorGroup.getParent()).getView();
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
