    public static Image createImage(GraphicalViewer viewer) {
        Image img = null;
        GC figureCanvasGC = null;
        GC imageGC = null;
        try {
            ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) viewer.getEditPartRegistry().get(LayerManager.ID);
            rootEditPart.refresh();
            IFigure rootFigure = ((LayerManager) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);
            EditPart editPart = viewer.getContents();
            editPart.refresh();
            ERDiagram diagram = (ERDiagram) editPart.getModel();
            Rectangle rootFigureBounds = getBounds(diagram, rootEditPart, rootFigure.getBounds());
            Control figureCanvas = viewer.getControl();
            figureCanvasGC = new GC(figureCanvas);
            img = new Image(Display.getCurrent(), rootFigureBounds.width + 20, rootFigureBounds.height + 20);
            imageGC = new GC(img);
            imageGC.setBackground(figureCanvasGC.getBackground());
            imageGC.setForeground(figureCanvasGC.getForeground());
            imageGC.setFont(figureCanvasGC.getFont());
            imageGC.setLineStyle(figureCanvasGC.getLineStyle());
            imageGC.setLineWidth(figureCanvasGC.getLineWidth());
            imageGC.setAntialias(SWT.OFF);
            Graphics imgGraphics = new SWTGraphics(imageGC);
            imgGraphics.setBackgroundColor(figureCanvas.getBackground());
            imgGraphics.fillRectangle(0, 0, rootFigureBounds.width + 20, rootFigureBounds.height + 20);
            imgGraphics.translate(translateX(rootFigureBounds.x), translateY(rootFigureBounds.y));
            rootFigure.paint(imgGraphics);
            return img;
        } finally {
            if (figureCanvasGC != null) {
                figureCanvasGC.dispose();
            }
            if (imageGC != null) {
                imageGC.dispose();
            }
        }
    }
