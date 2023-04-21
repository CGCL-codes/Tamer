    public void writeToStream(String urlString, OutputStream os) {
        BufferedInputStream input = null;
        try {
            URL url = new URL(urlString);
            logger.debug("Opening stream:" + url.toString());
            input = new BufferedInputStream(url.openStream(), 4 * 1024 * 1024);
            byte[] data = new byte[102400];
            int read;
            while ((read = input.read(data)) != -1) {
                os.write(data, 0, read);
            }
        } catch (Exception e) {
            logger.debug("Error writing to stream:", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.debug("Error closing stream:", e);
                }
            }
        }
    }
