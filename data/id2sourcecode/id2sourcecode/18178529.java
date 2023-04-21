    public static boolean copyStreams(InputStream is, OutputStream os, StopDownloadCondition condition) throws IOException {
        byte[] buffer = new byte[8192];
        int read = -1;
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
            if (condition.isStopped()) {
                LOGGER.info("stopped copying stream");
                return true;
            }
        }
        return false;
    }
