    public void execute(IFile erdFile, RootModel root, GraphicalViewer viewer) {
        ScalableRootEditPart rootEditPart = (ScalableRootEditPart) viewer.getRootEditPart();
        double zoom = rootEditPart.getZoomManager().getZoom();
        try {
            FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
            dialog.setFileName(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getTitle() + ".jpg");
            String file = dialog.open();
            if (file != null) {
                IFigure figure = rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
                Rectangle rectangle = figure.getBounds();
                Image image = new Image(Display.getDefault(), rectangle.width + 50, rectangle.height + 50);
                GC gc = new GC(image);
                SWTGraphics graphics = new SWTGraphics(gc);
                figure.paint(graphics);
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                if (file.endsWith(".bmp")) {
                    loader.save(file, SWT.IMAGE_BMP);
                } else if (file.endsWith(".gif")) {
                    loader.save(file, SWT.IMAGE_GIF);
                } else if (file.endsWith(".jpg") || file.endsWith(".jpeg")) {
                    loader.save(file, SWT.IMAGE_JPEG);
                } else if (file.endsWith(".png")) {
                    loader.save(file, SWT.IMAGE_PNG);
                } else if (file.endsWith(".tiff")) {
                    loader.save(file, SWT.IMAGE_TIFF);
                } else {
                    file = file + ".bmp";
                    loader.save(file, SWT.IMAGE_BMP);
                }
                image.dispose();
                gc.dispose();
            }
        } catch (Exception ex) {
        } finally {
            rootEditPart.getZoomManager().setZoom(zoom);
        }
    }
