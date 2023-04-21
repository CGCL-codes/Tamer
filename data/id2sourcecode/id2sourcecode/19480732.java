    public boolean hasNewerVersion() {
        try {
            InputStream input = url.openStream();
            try {
                byte[] bytes = ByteStreams.toByteArray(input);
                if (bytes == null || bytes.length == 0) {
                    throw new IOException("Downloaded empty contents");
                }
                Version current = new Version(configuration.getApplicationVersion());
                Version remote = new Version(new String(bytes));
                log.info("Version check: current = " + current + "; remote = " + remote);
                return remote.compareTo(current) > 0;
            } finally {
                input.close();
            }
        } catch (IOException e) {
            log.warn("Unable to check the new version", e);
            return false;
        } catch (NumberFormatException e) {
            log.warn("Unable to check the new version", e);
            return false;
        }
    }
