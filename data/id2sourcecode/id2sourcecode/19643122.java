    public void setInput(IEditorInput input) {
        ElementEditorInput shapesInput = ((ElementEditorInput) input);
        diagram = shapesInput.getPageDiagram();
        setPartName(shapesInput.getName());
        setInputWithNotify(input);
        this.firePropertyChange(IEditorPart.PROP_INPUT);
        if (getGraphicalViewer() != null) getGraphicalViewer().setContents(getModel());
    }
