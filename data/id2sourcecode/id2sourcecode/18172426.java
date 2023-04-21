    protected InputStream openStream(String url) throws IOException {
        final int tryNumber = 10;
        IOException lastError = null;
        URL net = new URL(url);
        for (int i = 0; i < tryNumber; ++i) {
            try {
                InputStream in = net.openStream();
                return in;
            } catch (IOException err) {
                lastError = err;
                LOG.info("Trying " + i + " " + err.getMessage());
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                }
                continue;
            }
        }
        throw lastError;
    }
