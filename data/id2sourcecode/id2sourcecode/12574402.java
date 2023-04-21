    @SuppressWarnings("deprecation")
    private static void saveEditorContentsAsImage(IEditorPart editorPart, GraphicalViewer viewer, String saveFilePath, int format) {
        FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
        IFigure rootFigure = figureCanvas.getContents();
        Rectangle rootFigureBounds = rootFigure.getBounds();
        GC figureCanvasGC = new GC(figureCanvas);
        Image img = new Image(null, rootFigureBounds.width, rootFigureBounds.height);
        GC imageGC = new GC(img);
        imageGC.setTransform(new Transform(null, 1, 0, 0, 1, -rootFigureBounds.x, -rootFigureBounds.y));
        imageGC.setBackground(figureCanvasGC.getBackground());
        imageGC.setForeground(figureCanvasGC.getForeground());
        imageGC.setFont(figureCanvasGC.getFont());
        imageGC.setLineStyle(figureCanvasGC.getLineStyle());
        imageGC.setLineWidth(figureCanvasGC.getLineWidth());
        imageGC.setXORMode(figureCanvasGC.getXORMode());
        Graphics imgGraphics = new SWTGraphics(imageGC);
        rootFigure.paint(imgGraphics);
        ImageData[] imgData = new ImageData[1];
        imgData[0] = img.getImageData();
        ImageLoader imgLoader = new ImageLoader();
        imgLoader.data = imgData;
        imgLoader.save(saveFilePath, format);
        figureCanvasGC.dispose();
        imageGC.dispose();
        img.dispose();
    }
