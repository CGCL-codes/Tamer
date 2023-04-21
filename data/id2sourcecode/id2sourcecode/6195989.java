    public BufferedImage JPEGToRGBImage(byte[] data, int w, int h, String decodeArray) {
        BufferedImage image = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            if (PdfDecoder.use13jPEGConversion) {
                com.sun.image.codec.jpeg.JPEGImageDecoder decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(bis);
                image = decoder.decodeAsBufferedImage();
                decoder = null;
            } else {
                ImageIO.setUseCache(false);
                image = ImageIO.read(bis);
            }
            if (image != null) image = ColorSpaceConvertor.convertToRGB(image);
        } catch (Exception ee) {
            image = null;
            LogWriter.writeLog("Problem reading JPEG: " + ee);
            ee.printStackTrace();
        }
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
