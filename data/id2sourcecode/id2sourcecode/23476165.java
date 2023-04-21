    @Override
    public void execute(Event event) {
        ERDiagram diagram = this.getDiagram();
        SearchDialog dialog = new SearchDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), this.getGraphicalViewer(), diagram);
        dialog.open();
    }
