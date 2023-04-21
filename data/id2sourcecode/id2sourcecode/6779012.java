    @Override
    public void execute(Event event) {
        ERDiagram diagram = this.getDiagram();
        ChangeTrackingDialog dialog = new ChangeTrackingDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), this.getGraphicalViewer(), diagram);
        dialog.open();
    }
