    private void copyResource(URL url, HttpServletResponse res) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = res.getOutputStream();
            URLConnection conn = url.openConnection();
            int len = conn.getContentLength();
            res.setContentLength(len);
            is = conn.getInputStream();
            len = 0;
            byte[] buf = new byte[1024];
            int n;
            while ((n = is.read(buf, 0, buf.length)) >= 0) {
                os.write(buf, 0, n);
                len += n;
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
