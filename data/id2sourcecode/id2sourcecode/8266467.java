    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
        if (viewer != null) {
            List<NodeElementEditPart> children = new ArrayList<NodeElementEditPart>();
            for (Object child : viewer.getContents().getChildren()) {
                if (child instanceof NodeElementEditPart) {
                    NodeElementEditPart editPart = (NodeElementEditPart) child;
                    if (editPart.getFigure().isVisible()) {
                        children.add(editPart);
                    }
                }
            }
            viewer.setSelection(new StructuredSelection(children));
        }
    }
