    public void saveImage() {
        if (this.pfad == null) {
            throw new IllegalAccessError("Pfad ist nicht angegeben");
        }
        if (this.filetype <= 0) {
            throw new IllegalAccessError("Filetype ist nicht angegeben");
        }
        GraphicalViewer viewer = this.getGraphicalViewer();
        ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart) viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = ((LayerManager) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);
        Rectangle rootFigureBounds = rootFigure.getBounds();
        Control figureCanvas = viewer.getControl();
        GC figureCanvasGC = new GC(figureCanvas);
        Image img = new Image(null, rootFigureBounds.width, rootFigureBounds.height);
        GC imageGC = new GC(img);
        imageGC.setBackground(figureCanvasGC.getBackground());
        imageGC.setForeground(figureCanvasGC.getForeground());
        imageGC.setFont(figureCanvasGC.getFont());
        imageGC.setLineStyle(figureCanvasGC.getLineStyle());
        imageGC.setLineWidth(figureCanvasGC.getLineWidth());
        Graphics imgGraphics = new SWTGraphics(imageGC);
        rootFigure.paint(imgGraphics);
        ImageData[] imgData = new ImageData[1];
        imgData[0] = img.getImageData();
        ImageLoader imgLoader = new ImageLoader();
        imgLoader.data = imgData;
        imgLoader.save(this.pfad, this.filetype);
        figureCanvasGC.dispose();
        imageGC.dispose();
        img.dispose();
    }
