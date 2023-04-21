    protected byte[] fetchClass(String classname) throws Exception {
        byte[] b;
        URL url = new URL("http", server, port, "/" + classname.replace('.', '/') + ".class");
        URLConnection con = url.openConnection();
        con.connect();
        int size = con.getContentLength();
        InputStream s = con.getInputStream();
        if (size <= 0) b = readStream(s); else {
            b = new byte[size];
            int len = 0;
            do {
                int n = s.read(b, len, size - len);
                if (n < 0) {
                    s.close();
                    throw new IOException("the stream was closed: " + classname);
                }
                len += n;
            } while (len < size);
        }
        s.close();
        return b;
    }
