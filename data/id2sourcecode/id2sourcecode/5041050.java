    public static boolean isImageOnWebServer(final URL url) {
        boolean onServer = false;
        if (url != null) {
            byte[] b = new byte[3];
            try {
                InputStream in = url.openStream();
                in.read(b);
                in.close();
            } catch (Exception e) {
                if (log.isInfoEnabled()) {
                    log.info("Unable to open the URL, " + url, e);
                }
            }
            if (Arrays.equals(b, PNG_KEY) || Arrays.equals(b, GIF_KEY) || Arrays.equals(b, JPG_KEY)) {
                onServer = true;
            }
        }
        return onServer;
    }
