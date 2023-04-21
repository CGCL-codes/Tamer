    private void fillPixelArrayWithRaster(int left, int top, int clipWidth, int clipHeight, int pixelToFill[][]) {
        Rectangle clipRect = new Rectangle(left, top, clipWidth, clipHeight);
        BufferedImage clipImage = robot.createScreenCapture(clipRect);
        int[] raster = clipImage.getData().getPixels(0, 0, clipWidth, clipHeight, (int[]) null);
        int rows = 0;
        int columns = 0;
        boolean isfirstcolumn = true;
        for (int i = 0; i < raster.length; i = i + 3) {
            if (((i / 3) % (clipWidth)) == 0) {
                rows = 0;
                if (!isfirstcolumn) {
                    columns++;
                }
            } else {
                rows++;
            }
            if (((i / 3) % clipHeight) == 0) {
                isfirstcolumn = false;
            }
            pixelToFill[columns][rows] = (raster[i + 2]) | (raster[i + 1] << 8) | (raster[i] << 16 | 0xFF000000);
        }
    }
