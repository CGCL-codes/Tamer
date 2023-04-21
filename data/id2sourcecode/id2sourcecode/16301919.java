    public final BufferedImage dataToRGB(byte[] data, int width, int height) {
        BufferedImage image = null;
        DataBuffer db = new DataBufferByte(data, data.length);
        int size = width * height;
        try {
            for (int i = 0; i < size * 3; i = i + 3) {
                float cl = db.getElemFloat(i);
                float ca = db.getElemFloat(i + 1);
                float cb = db.getElemFloat(i + 2);
                convertToRGB(cl, ca, cb);
                db.setElem(i, r);
                db.setElem(i + 1, g);
                db.setElem(i + 2, b);
            }
            int[] bands = { 0, 1, 2 };
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Raster raster = Raster.createInterleavedRaster(db, width, height, width * 3, 3, bands, null);
            image.setData(raster);
        } catch (Exception ee) {
            image = null;
            LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
        }
        return image;
    }
