    public static final Image loadImage(final String fileName) {
        String keyName = fileName.trim().toLowerCase();
        Image cacheImage = (Image) cacheImages.get(keyName);
        if (cacheImage == null) {
            InputStream in = new BufferedInputStream(classLoader.getResourceAsStream(fileName));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byte[] bytes = new byte[8192];
                int read;
                while ((read = in.read(bytes)) >= 0) {
                    byteArrayOutputStream.write(bytes, 0, read);
                }
                byte[] arrayByte = byteArrayOutputStream.toByteArray();
                cacheImages.put(keyName, cacheImage = toolKit.createImage(arrayByte));
                mediaTracker.addImage(cacheImage, 0);
                mediaTracker.waitForID(0);
                waitImage(100, cacheImage);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                        byteArrayOutputStream = null;
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        if (cacheImage == null) {
            throw new RuntimeException(("File not found. ( " + fileName + " )").intern());
        }
        return cacheImage;
    }
