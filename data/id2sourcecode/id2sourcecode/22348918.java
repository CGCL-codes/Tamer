    private void real_download(URL url, OutputStream pipe) throws Exception {
        ByteArrayOutputStream d = new ByteArrayOutputStream();
        InputStream s = null;
        qaop.repaint(x, y, w, h);
        try {
            URLConnection con = url.openConnection();
            int l = con.getContentLength();
            flength = l > 0 ? l : 1 << 16;
            s = con.getInputStream();
            if (con instanceof java.net.HttpURLConnection) {
                int c = ((java.net.HttpURLConnection) con).getResponseCode();
                if (c < 200 || c > 299) throw new FileNotFoundException();
            }
            byte buf[] = new byte[4096];
            for (; ; ) {
                if (interrupted()) throw new InterruptedException();
                int n = s.available();
                if (n < 1) n = 1; else if (n > buf.length) n = buf.length;
                n = s.read(buf, 0, n);
                if (n <= 0) break;
                d.write(buf, 0, n);
                pipe.write(buf, 0, n);
                floaded += n;
                qaop.repaint(x, y, w, h);
            }
            cache.put(url, d.toByteArray());
        } finally {
            if (s != null) try {
                s.close();
            } catch (IOException e) {
            }
        }
    }
