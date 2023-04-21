    private void setDiagramSelection(ISelection selection) {
        if (isDispatching) {
            ArrayList<EditPart> result = new ArrayList<EditPart>();
            for (Object obj : ((StructuredSelection) selection).toList()) {
                EditPart part = searchEditPart((EObject) obj);
                if (part != null) result.add(part);
            }
            consumeNextSelection = true;
            editor.getDiagramGraphicalViewer().setSelection(new StructuredSelection(result));
            if (result.size() > 0) editor.getDiagramGraphicalViewer().reveal((EditPart) result.get(result.size() - 1));
        }
    }
