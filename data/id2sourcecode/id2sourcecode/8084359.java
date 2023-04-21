    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        if (viewer != null) {
            Class clazz = ((EditPart) getSelectedObjects().get(0)).getModel().getClass();
            List<AbstractGraphicalEditPart> list = new ArrayList<AbstractGraphicalEditPart>();
            visit((AbstractGraphicalEditPart) viewer.getContents(), list, clazz);
            viewer.setSelection(new StructuredSelection(list));
        }
    }
