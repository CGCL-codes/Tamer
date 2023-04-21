    @SuppressWarnings("deprecation")
    public static void print(IEditorPart editorPart, GraphicalViewer viewer, String printJobName) {
        PrintDialog dialog = new PrintDialog(editorPart.getSite().getShell());
        PrinterData printerData = dialog.open();
        if (printerData != null) {
            Printer printer = new Printer(printerData);
            if (printer.startJob(printJobName)) {
                GC printerGC = new GC(printer);
                if (printer.startPage()) {
                    org.eclipse.swt.graphics.Rectangle printableBounds = printer.getClientArea();
                    printerGC.drawString(printJobName, 100, printableBounds.height - 100);
                    FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
                    IFigure rootFigure = figureCanvas.getContents();
                    Rectangle rootFigureBounds = rootFigure.getBounds();
                    float scaleX = (float) printableBounds.width / rootFigureBounds.width;
                    float scaleY = (float) printableBounds.height / rootFigureBounds.height;
                    float scale = Math.min(scaleX, scaleY);
                    Point screenDPI = figureCanvas.getDisplay().getDPI();
                    Point printerDPI = printer.getDPI();
                    float scaleFactor = (float) printerDPI.x / screenDPI.x;
                    org.eclipse.swt.graphics.Rectangle trim = printer.computeTrim(0, 0, 0, 0);
                    scale /= scaleFactor;
                    scale *= 0.8f;
                    GC figureCanvasGC = new GC(figureCanvas);
                    float dx = -rootFigureBounds.x;
                    dx *= scale;
                    dx -= trim.x;
                    dx += 0.1f * trim.width;
                    float dy = -rootFigureBounds.x;
                    dy *= scale;
                    dy -= trim.y;
                    dy += 0.1f * trim.height;
                    printerGC.setTransform(new Transform(null, scale, 0, 0, scale, dx, dy));
                    printerGC.setBackground(figureCanvasGC.getBackground());
                    printerGC.setForeground(figureCanvasGC.getForeground());
                    printerGC.setFont(figureCanvasGC.getFont());
                    printerGC.setLineStyle(figureCanvasGC.getLineStyle());
                    printerGC.setLineWidth(figureCanvasGC.getLineWidth());
                    printerGC.setXORMode(figureCanvasGC.getXORMode());
                    Graphics printerGraphics = new SWTGraphics(printerGC);
                    rootFigure.paint(printerGraphics);
                    printer.endPage();
                    figureCanvasGC.dispose();
                    printerGraphics.dispose();
                }
                printerGC.dispose();
                printer.endJob();
            }
            printer.dispose();
        }
    }
