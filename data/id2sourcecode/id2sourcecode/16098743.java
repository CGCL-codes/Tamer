    protected void doGotoMarker(IMarker marker) {
        String elementId = marker.getAttribute(org.eclipse.gmf.runtime.common.ui.resources.IMarker.ELEMENT_ID, null);
        if (elementId == null || !(getEditor() instanceof DiagramEditor)) {
            return;
        }
        EditPart targetEditPart = null;
        DiagramEditor editor = (DiagramEditor) getEditor();
        Map epartRegistry = editor.getDiagramGraphicalViewer().getEditPartRegistry();
        for (Iterator it = epartRegistry.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey() instanceof View) {
                View view = (View) entry.getKey();
                String viewId = ViewUtil.getIdStr(view);
                if (viewId.equals(elementId)) {
                    targetEditPart = (EditPart) entry.getValue();
                    break;
                }
            }
        }
        if (targetEditPart != null) {
            editor.getDiagramGraphicalViewer().select(targetEditPart);
            editor.getDiagramGraphicalViewer().reveal(targetEditPart);
        }
    }
