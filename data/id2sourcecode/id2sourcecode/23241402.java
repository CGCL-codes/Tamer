    @Override
    public void run() {
        IWorkbenchPart part = getWorkbenchPart();
        if (part != null) {
            GraphicalViewer viewer = (GraphicalViewer) part.getAdapter(GraphicalViewer.class);
            if (viewer != null) {
                int style = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getStyle();
                Shell shell = new Shell((style & SWT.MIRRORED) != 0 ? SWT.RIGHT_TO_LEFT : SWT.NONE);
                PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
                PrinterData data = dialog.open();
                if (data != null) {
                    PrintGraphicalViewerOperation operation = new PrintGraphicalViewerOperation(new Printer(data), viewer);
                    operation.setPrintMode(2);
                    operation.run("Printing...");
                }
            }
        }
    }
