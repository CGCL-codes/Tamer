    protected final LoadReturnCode loadInternal(Properties overwrites, URL url) throws IOException {
        InputStream inStream = url.openStream();
        try {
            return loadInternal(overwrites, inStream, url.toString());
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                    inStream = null;
                }
            } catch (IOException ioex) {
            }
        }
    }
