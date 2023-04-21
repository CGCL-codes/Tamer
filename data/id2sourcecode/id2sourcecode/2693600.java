    protected static byte[] retrieveURL(final String url) throws Exception {
        try {
            final HttpURLConnection uc = (HttpURLConnection) new URL(url).openConnection();
            try {
                byte[] buf = new byte[1024];
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                InputStream in = uc.getInputStream();
                while (true) {
                    int rc = in.read(buf);
                    if (rc <= 0) break;
                    bout.write(buf, 0, rc);
                }
                return bout.toByteArray();
            } finally {
                uc.getInputStream().close();
                if (uc.getErrorStream() != null) {
                    uc.getErrorStream().close();
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error retrieving data: " + ex.getMessage(), ex);
        }
    }
