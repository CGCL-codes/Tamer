    public BufferedImage JPEGToRGBImage(byte[] data, int ww, int hh, String decodeArray) {
        BufferedImage image = null;
        ByteArrayInputStream in = null;
        ImageReader iir = null;
        ImageInputStream iin = null;
        try {
            in = new ByteArrayInputStream(data);
            iir = (ImageReader) ImageIO.getImageReadersByFormatName("JPEG").next();
            ImageIO.setUseCache(false);
            iin = ImageIO.createImageInputStream((in));
            iir.setInput(iin, true);
            Raster r = iir.readRaster(0, null);
            int w = r.getWidth(), h = r.getHeight();
            DataBufferByte rgb = (DataBufferByte) r.getDataBuffer();
            image = createImage(w, h, rgb.getData());
        } catch (Exception ee) {
            image = null;
            LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
        }
        try {
            in.close();
            iir.dispose();
            iin.close();
        } catch (Exception ee) {
            LogWriter.writeLog("Problem closing  " + ee);
        }
        return image;
    }
