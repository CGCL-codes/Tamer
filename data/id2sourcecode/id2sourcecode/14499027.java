    public static final Image loadNotCacheImage(final String innerFileName) {
        if (innerFileName == null) {
            return null;
        }
        int read;
        byte[] bytes = null;
        Image img_tmp = null;
        InputStream in = null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream(8192);
            in = new DataInputStream(new BufferedInputStream(Resources.openResource(innerFileName)));
            bytes = new byte[8192];
            while ((read = in.read(bytes)) >= 0) {
                os.write(bytes, 0, read);
            }
            bytes = os.toByteArray();
            img_tmp = toolKit.createImage(bytes);
            waitImage(img_tmp);
        } catch (Exception e) {
            throw new RuntimeException(("File not found. ( " + innerFileName + " )").intern());
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
                bytes = null;
            } catch (IOException e) {
            }
        }
        return img_tmp;
    }
