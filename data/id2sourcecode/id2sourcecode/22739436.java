    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        int printMode = new PrintModeDialog(viewer.getControl().getShell()).open();
        if (printMode == -1) {
            return;
        }
        PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
        PrinterData data = dialog.open();
        if (data != null) {
            PrintGraphicalViewerOperation op = new PrintGraphicalViewerOperation(new Printer(data), viewer);
            op.setPrintMode(printMode);
            op.run(getWorkbenchPart().getTitle());
        }
    }
