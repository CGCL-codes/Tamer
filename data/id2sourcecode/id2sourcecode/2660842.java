    private void resetInput() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
        IEditorInput input = mLayoutEditor.getEditorInput();
        setInput(input);
    }
