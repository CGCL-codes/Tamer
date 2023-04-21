    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof EJBTool.persistent.navigator.EJBToolAbstractNavigatorItem) {
            return;
        }
        EJBTool.persistent.navigator.EJBToolAbstractNavigatorItem abstractNavigatorItem = (EJBTool.persistent.navigator.EJBToolAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof EJBTool.persistent.navigator.EJBToolNavigatorItem) {
            navigatorView = ((EJBTool.persistent.navigator.EJBToolNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof EJBTool.persistent.navigator.EJBToolNavigatorGroup) {
            EJBTool.persistent.navigator.EJBToolNavigatorGroup navigatorGroup = (EJBTool.persistent.navigator.EJBToolNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof EJBTool.persistent.navigator.EJBToolNavigatorItem) {
                navigatorView = ((EJBTool.persistent.navigator.EJBToolNavigatorItem) navigatorGroup.getParent()).getView();
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
