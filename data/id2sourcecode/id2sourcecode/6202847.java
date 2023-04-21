    public String read() throws IOException {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();
        int length = conn.getContentLength();
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = conn.getInputStream();
            out = new ByteArrayOutputStream();
            int buflen = 1024 * 30;
            int bytesRead = 0;
            byte[] buf = new byte[buflen];
            ;
            for (int nRead = in.read(buf); nRead != -1; nRead = in.read(buf)) {
                bytesRead += nRead;
                out.write(buf, 0, nRead);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return out != null ? new String(out.toByteArray()) : null;
    }
