    public static Image getImageFromJar(final String imageId, Class c) {
        Image image = null;
        final InputStream inputStream = c.getResourceAsStream(imageId);
        if (inputStream != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                final byte[] bytes = new byte[1024];
                int read = 0;
                while ((read = inputStream.read(bytes)) >= 0) {
                    byteArrayOutputStream.write(bytes, 0, read);
                }
                image = Toolkit.getDefaultToolkit().createImage(byteArrayOutputStream.toByteArray());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return image;
    }
