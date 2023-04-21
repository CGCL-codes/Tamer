    public void testTestSocket() {
        try {
            Robot robot = new Robot();
            Rectangle screenRectangle = new Rectangle(0, 0, 400, 400);
            BufferedImage imageScreen = robot.createScreenCapture(screenRectangle);
            int scaledWidth = Math.round(400 * ClientConnectionBean.imgQuality);
            int scaledHeight = Math.round(400 * ClientConnectionBean.imgQuality);
            Image img = imageScreen.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            BufferedImage image = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D biContext = image.createGraphics();
            biContext.drawImage(img, 0, 0, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam encpar = encoder.getDefaultJPEGEncodeParam(image);
            encpar.setQuality(ClientConnectionBean.imgQuality, false);
            encoder.setJPEGEncodeParam(encpar);
            encoder.encode(image);
            imageScreen.flush();
            byte[] payload = out.toByteArray();
            ByteArrayOutputStream byteGzipOut = new ByteArrayOutputStream();
            GZIPOutputStream gZipOut = new GZIPOutputStream(byteGzipOut);
            gZipOut.write(payload);
            gZipOut.close();
            log.debug("byteGzipOut LENGTH " + byteGzipOut.toByteArray().length);
            log.debug("payload LENGTH " + payload.length);
            log.debug("JPEG RAW: " + payload.length);
            log.debug("JPEG GZIP: " + byteGzipOut.toByteArray().length);
            String imagePath_1 = "pic_.jpg";
            FileOutputStream fos_1 = new FileOutputStream(imagePath_1);
            fos_1.write(payload);
            fos_1.close();
            byte[] myBytes = byteGzipOut.toByteArray();
            String imagePath = "pic_.gzip";
            FileOutputStream fos = new FileOutputStream(imagePath);
            fos.write(myBytes);
            fos.close();
            ByteArrayInputStream byteGzipIn = new ByteArrayInputStream(myBytes);
            GZIPInputStream gZipIn = new GZIPInputStream(byteGzipIn);
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = gZipIn.read(buffer)) > 0) {
                bytesOut.write(buffer, 0, count);
            }
            bytesOut.close();
            gZipIn.close();
            log.debug("gZipIn CLosed");
        } catch (Exception err) {
            log.error("[TestSocket] ", err);
        }
    }
