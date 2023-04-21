    protected final BufferedImage nonRGBJPEGToRGBImage(byte[] data, int w, int h, String decodeArray) {
        boolean isProcessed = false;
        BufferedImage image = null;
        ByteArrayInputStream in = null;
        ImageReader iir = null;
        ImageInputStream iin = null;
        try {
            if (CSToRGB == null) initCMYKColorspace();
            CSToRGB = new ColorConvertOp(cs, rgbCS, ColorSpaces.hints);
            in = new ByteArrayInputStream(data);
            int cmykType = getJPEGTransform(data);
            Iterator iterator = ImageIO.getImageReadersByFormatName("JPEG");
            while (iterator.hasNext()) {
                Object o = iterator.next();
                iir = (ImageReader) o;
                if (iir.canReadRaster()) break;
            }
            ImageIO.setUseCache(false);
            iin = ImageIO.createImageInputStream((in));
            iir.setInput(iin, true);
            Raster ras = iir.readRaster(0, null);
            if (decodeArray != null) {
                if (decodeArray.indexOf("1 0 1 0 1 0 1 0") != -1) {
                    DataBuffer buf = ras.getDataBuffer();
                    int count = buf.getSize();
                    for (int ii = 0; ii < count; ii++) buf.setElem(ii, 255 - buf.getElem(ii));
                } else if (decodeArray.indexOf("0 1 0 1 0 1 0 1") != -1) {
                } else if (decodeArray.indexOf("0.0 1.0 0.0 1.0 0.0 1.0 0.0 1.0") != -1) {
                } else if (decodeArray.length() > 0) {
                }
            }
            if (cs.getNumComponents() == 4) {
                isProcessed = true;
                try {
                    if (cmykType == 2) {
                        image = ColorSpaceConvertor.algorithmicConvertCMYKImageToRGB(ras.getDataBuffer(), w, h, false);
                    } else {
                        WritableRaster rgbRaster = rgbModel.createCompatibleWritableRaster(w, h);
                        CSToRGB.filter(ras, rgbRaster);
                        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                        image.setData(rgbRaster);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (cmykType != 0) {
                image = iir.read(0);
                isProcessed = true;
            }
            if (!isProcessed) {
                WritableRaster rgbRaster;
                in = new ByteArrayInputStream(data);
                com.sun.image.codec.jpeg.JPEGImageDecoder decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(in);
                Raster currentRaster = decoder.decodeAsRaster();
                int colorType = decoder.getJPEGDecodeParam().getEncodedColorID();
                int width = currentRaster.getWidth();
                int height = currentRaster.getHeight();
                if (colorType == 4) {
                    rgbRaster = rgbModel.createCompatibleWritableRaster(width, height);
                    CSToRGB.filter(currentRaster, rgbRaster);
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    image.setData(rgbRaster);
                } else {
                    LogWriter.writeLog("COLOR_ID_YCbCrA image");
                    in = new ByteArrayInputStream(data);
                    decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(in);
                    image = decoder.decodeAsBufferedImage();
                    image = ColorSpaceConvertor.convertToRGB(image);
                }
            }
        } catch (Exception ee) {
            image = null;
            ee.printStackTrace();
            LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
        } catch (Error err) {
            if (iir != null) iir.dispose();
            if (iin != null) {
                try {
                    iin.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
