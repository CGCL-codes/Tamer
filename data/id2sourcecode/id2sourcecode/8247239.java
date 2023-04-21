    @Override
    protected void doGotoMarker(IMarker marker) {
        String elementId = marker.getAttribute(org.eclipse.gmf.runtime.common.core.resources.IMarker.ELEMENT_ID, null);
        if (elementId == null || !(this.getEditor() instanceof DiagramEditor)) {
            return;
        }
        DiagramEditor editor = (DiagramEditor) this.getEditor();
        Map<?, ?> editPartRegistry = editor.getDiagramGraphicalViewer().getEditPartRegistry();
        EObject targetView = editor.getDiagram().eResource().getEObject(elementId);
        if (targetView == null) {
            return;
        }
        EditPart targetEditPart = (EditPart) editPartRegistry.get(targetView);
        if (targetEditPart != null) {
            ParallelJDiagramEditorUtil.selectElementsInDiagram(editor, Arrays.asList(new EditPart[] { targetEditPart }));
        }
    }
