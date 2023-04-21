    public void takeAndSaveImage(Robot rt, float compressionQuality, int colorQuality, double scaleAbsolute, Rectangle screenRect) throws IOException {
        if (screenRect.equals(Commons.emptyRect)) screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenImage = rt.createScreenCapture(screenRect);
        int pieceWidth = (int) screen.getScreenBlockWidth();
        int pieceHeight = (int) screen.getScreenBlockHeight();
        int newW = (int) (pieceWidth * scaleAbsolute);
        int newH = (int) (pieceHeight * scaleAbsolute);
        BufferedImage subImage = new BufferedImage(newW, newH, colorQuality);
        screen.getChangedScreenBlocks().clear();
        modified = 0;
        for (int y = 0; y < screen.getRowScreenBlocks(); y++) for (int x = 0; x < screen.getColumnScreenBlocks(); x++) {
            String name = screen.getScreenBlockName(y, x);
            int startx = pieceWidth * x;
            int starty = pieceHeight * y;
            Graphics2D subImageGraphics = subImage.createGraphics();
            subImageGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            subImageGraphics.drawImage(screenImage, 0, 0, newW, newH, startx, starty, startx + pieceWidth, starty + pieceHeight, null);
            if (screen.getScreenBlockImage(name).isModified(ImageUtility.toByteArray(subImage, compressionQuality), ScreenBlock.COMPARE_LENGTH)) {
                screen.getChangedScreenBlocks().addElement(name);
                modified++;
            }
        }
    }
