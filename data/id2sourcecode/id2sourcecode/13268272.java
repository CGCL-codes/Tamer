    public DiagramPrintOperation(Printer p, GraphicalViewer g) {
        super(p, g);
        scale = getPrinter().getDPI().x / Display.getDefault().getDPI().x;
    }
