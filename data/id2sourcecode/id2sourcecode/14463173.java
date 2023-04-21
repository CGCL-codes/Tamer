    protected FileType doGetType() throws Exception {
        try {
            final URLConnection conn = url.openConnection();
            final InputStream in = conn.getInputStream();
            try {
                if (conn instanceof HttpURLConnection) {
                    final int status = ((HttpURLConnection) conn).getResponseCode();
                    if (HttpURLConnection.HTTP_OK != status) {
                        return FileType.IMAGINARY;
                    }
                }
                return FileType.FILE;
            } finally {
                in.close();
            }
        } catch (final FileNotFoundException e) {
            return FileType.IMAGINARY;
        }
    }
