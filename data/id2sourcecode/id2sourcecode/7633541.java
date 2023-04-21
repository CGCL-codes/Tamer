    public static byte[] derefUrl(URL url) {
        byte[] ba = null;
        InputStream is = null;
        try {
            URLConnection conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            ba = Utils.getBytesToEndOfStream(is, true);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            return null;
        }
        return ba;
    }
