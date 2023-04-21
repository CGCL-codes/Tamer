    public BufferedImage JPEGToRGBImage(byte[] data, int w, int h, String decodeArray) {
        BufferedImage image = null;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(data);
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
            Raster currentRaster = decoder.decodeAsRaster();
            final int width = currentRaster.getWidth();
            final int height = currentRaster.getHeight();
            final int imgSize = width * height;
            DataBuffer db = currentRaster.getDataBuffer();
            for (int i = 0; i < imgSize * 3; i = i + 3) {
                float cl = db.getElemFloat(i) * C4;
                float ca = db.getElemFloat(i + 1) - C5;
                float cb = db.getElemFloat(i + 2) - C5;
                convertToRGB(cl, ca, cb);
                db.setElem(i, r);
                db.setElem(i + 1, g);
                db.setElem(i + 2, b);
            }
            int[] bands = { 0, 1, 2 };
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Raster raster = Raster.createInterleavedRaster(db, width, height, width * 3, 3, bands, null);
            image.setData(raster);
            in.close();
        } catch (Exception ee) {
            image = null;
            LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
        }
        return image;
    }
