    public static byte[] readResourceAsBytes(String path) {
        byte[] byteData = null;
        try {
            InputStream is = FileUtil.class.getResourceAsStream(path);
            if (is.available() > Integer.MAX_VALUE) {
                throw new OopsException("Oversized file :-( can't read it, sorry: " + path);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[512];
            int readBytes;
            while ((readBytes = is.read(bytes)) > 0) {
                os.write(bytes, 0, readBytes);
            }
            byteData = os.toByteArray();
            is.close();
            os.close();
        } catch (Exception e) {
            throw new OopsException(e, "Problems when reading: [" + path + "].");
        }
        return byteData;
    }
