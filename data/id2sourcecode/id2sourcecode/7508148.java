    public void testTestSocket() {
        try {
            Robot robot = new Robot();
            Rectangle screenRectangle = new Rectangle(0, 0, 400, 400);
            BufferedImage imageScreen = robot.createScreenCapture(screenRectangle);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam encpar = encoder.getDefaultJPEGEncodeParam(imageScreen);
            encpar.setQuality(ClientConnectionBean.imgQuality, false);
            encoder.setJPEGEncodeParam(encpar);
            encoder.encode(imageScreen);
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
