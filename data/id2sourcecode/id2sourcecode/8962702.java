    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
        if (viewer != null) {
            Collection c = viewer.getEditPartRegistry().values();
            List selection = new ArrayList();
            for (Iterator iter = c.iterator(); iter.hasNext(); ) {
                EditPart part = (EditPart) iter.next();
                if (part != viewer.getRootEditPart() && !viewer.getRootEditPart().getChildren().contains(part)) selection.add(part);
            }
            viewer.setSelection(new StructuredSelection(selection));
        }
    }
