    public static boolean isImage(final URL url) throws IOException {
        final InputStream inputStream = url.openStream();
        try {
            return ImageIO.read(inputStream) != null;
        } finally {
            inputStream.close();
        }
    }
