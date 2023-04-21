    public synchronized Image createScreenCapture(Rectangle rect) {
        PaletteData pData = new PaletteData(255 << 16, 255 << 8, 255);
        ImageData iData = new ImageData(rect.width, rect.height, 24, pData);
        Color color;
        for (int x = 0; x < rect.width; x++) {
            for (int y = 0; y < rect.height; y++) {
                color = getPixelColor(rect.x + x, rect.y + y);
                iData.setPixel(x, y, color.getRed() << 16 | color.getGreen() << 8 | color.getBlue());
            }
        }
        Image image = new Image(Display.getDefault(), iData);
        return image;
    }
