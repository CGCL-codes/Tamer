    public static Image loadImage(String str) {
        if (str == null) {
            return null;
        }
        Image cacheImage = (Image) cacheImages.get(str.toLowerCase());
        ;
        if (cacheImage == null) {
            InputStream in = new BufferedInputStream(classLoader.getResourceAsStream(str));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byte[] bytes = new byte[16384];
                int read;
                while ((read = in.read(bytes)) >= 0) {
                    byteArrayOutputStream.write(bytes, 0, read);
                }
                bytes = byteArrayOutputStream.toByteArray();
                cacheImages.put(str.toLowerCase(), cacheImage = toolKit.createImage(bytes));
                mediaTracker.addImage(cacheImage, 0);
                mediaTracker.waitForID(0);
                waitImage(100, cacheImage);
            } catch (Exception e) {
                throw new RuntimeException(str + " not found!");
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                        byteArrayOutputStream = null;
                    }
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                } catch (IOException e) {
                }
            }
        }
        if (cacheImage == null) {
            throw new RuntimeException(("File not found. ( " + str + " )").intern());
        }
        return cacheImage;
    }
