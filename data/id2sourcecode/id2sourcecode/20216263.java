    private static byte[] getBytes(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        if (uc instanceof java.net.HttpURLConnection) {
            java.net.HttpURLConnection huc = (java.net.HttpURLConnection) uc;
            int code = huc.getResponseCode();
            if (code >= java.net.HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new IOException("open HTTP connection failed.");
            }
        }
        int len = uc.getContentLength();
        InputStream in = uc.getInputStream();
        byte[] b;
        try {
            if (len != -1) {
                b = new byte[len];
                while (len > 0) {
                    int n = in.read(b, b.length - len, len);
                    if (n == -1) {
                        throw new IOException("unexpected EOF");
                    }
                    len -= n;
                }
            } else {
                b = new byte[1024];
                int total = 0;
                while ((len = in.read(b, total, b.length - total)) != -1) {
                    total += len;
                    if (total >= b.length) {
                        byte[] tmp = new byte[total * 2];
                        System.arraycopy(b, 0, tmp, 0, total);
                        b = tmp;
                    }
                }
                if (total != b.length) {
                    byte[] tmp = new byte[total];
                    System.arraycopy(b, 0, tmp, 0, total);
                    b = tmp;
                }
            }
        } finally {
            in.close();
        }
        return b;
    }
