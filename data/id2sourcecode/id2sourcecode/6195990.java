    public BufferedImage JPEG2000ToRGBImage(byte[] data) throws PdfException {
        BufferedImage image = null;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(data);
            ImageReader iir = (ImageReader) ImageIO.getImageReadersByFormatName("JPEG2000").next();
            ImageInputStream iin = ImageIO.createImageInputStream(in);
            try {
                iir.setInput(iin, true);
                image = iir.read(0);
                iir.dispose();
                iin.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            image = ColorSpaceConvertor.convertToRGB(image);
        } catch (Exception ee) {
            image = null;
            LogWriter.writeLog("Problem reading JPEG 2000: " + ee);
            throw new PdfException("Exception with JPEG2000 image - please ensure imageio.jar (from JAI library) on classpath");
        } catch (Error ee2) {
            image = null;
            ee2.printStackTrace();
            LogWriter.writeLog("Problem reading JPEG 2000: " + ee2);
            throw new PdfException("Error with JPEG2000 image - please ensure imageio.jar (from JAI library) on classpath");
        }
        return image;
    }
