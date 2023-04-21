    private static List<BufferedImage> getFaviconsFromURL(URL url) {
        List<BufferedImage> images = null;
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            images = ICODecoder.read(inputStream);
        } catch (Exception e) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.trace("Couldn't close inputStream");
            }
        }
        return images;
    }
