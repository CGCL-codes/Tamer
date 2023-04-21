    public void run() {
        GraphicalViewer viewer;
        viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
        PrinterData data = dialog.open();
        if (data != null) {
            PrintGraphicalViewerOperation op = new PrintGraphicalViewerOperation(new Printer(data), viewer);
            op.run(getWorkbenchPart().getTitle());
        }
    }
