    public void run() {
        GraphicalViewer viewer;
        viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure layer = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
        PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), layer, true);
        dialog.open();
        PrinterData data = dialog.getPrinter();
        if (data != null) {
            RMBenchPrintOperation op = new RMBenchPrintOperation(new Printer(data), dialog.getPages(), viewer);
            double margin = RMBenchPlugin.getPrintState().margin;
            op.setMargin((int) (margin * Display.getCurrent().getDPI().x));
            op.setPrintMode(dialog.getPrintMode());
            op.run(getWorkbenchPart().getTitle());
        }
    }
