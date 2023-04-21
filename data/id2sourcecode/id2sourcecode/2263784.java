    @Override
    public void run() {
        GraphicalViewer viewer;
        viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
        PrinterData data = dialog.open();
        if (data != null) {
            Printer printer = new Printer(data);
            PrintGraphicalViewerOperation op = new PrintERDiagramOperation(printer, viewer);
            op.run(getWorkbenchPart().getTitle());
        }
    }
