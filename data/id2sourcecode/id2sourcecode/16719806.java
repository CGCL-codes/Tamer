    public static void transfer(InputStream in, OutputStream out) throws IOException {
        try {
            long bytesSoFar = 0;
            byte[] buffer = new byte[65535];
            int read;
            while ((read = in.read(buffer)) > -1) {
                if (read > 0) {
                    out.write(buffer, 0, read);
                    bytesSoFar += read;
                }
            }
        } finally {
            closeStream(in);
            closeStream(out);
        }
    }
