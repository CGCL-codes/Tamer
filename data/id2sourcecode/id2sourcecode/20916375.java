    public static boolean exportAsImage(IEditorPart editorPart, GraphicalViewer graphicalViewer, String saveFilePath, int format) {
        try {
            ScalableFreeformRootEditPart editPart = (ScalableFreeformRootEditPart) graphicalViewer.getEditPartRegistry().get(LayerManager.ID);
            IFigure rootFigure = ((LayerManager) editPart).getLayer(LayerConstants.PRINTABLE_LAYERS);
            Rectangle figureBounds = rootFigure.getBounds();
            Control figureCanvas = graphicalViewer.getControl();
            GC figureCanvasGC = new GC(figureCanvas);
            Image image = new Image(null, figureBounds.width, figureBounds.height);
            GC imageGC = new GC(image);
            imageGC.setBackground(figureCanvasGC.getBackground());
            imageGC.setForeground(figureCanvasGC.getForeground());
            imageGC.setFont(figureCanvasGC.getFont());
            imageGC.setLineStyle(figureCanvasGC.getLineStyle());
            imageGC.setLineWidth(figureCanvasGC.getLineWidth());
            Graphics imgGraphics = new SWTGraphics(imageGC);
            rootFigure.paint(imgGraphics);
            ImageData[] imageData = new ImageData[1];
            imageData[0] = image.getImageData();
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.data = imageData;
            imageLoader.save(saveFilePath, format);
            figureCanvasGC.dispose();
            imageGC.dispose();
            image.dispose();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
