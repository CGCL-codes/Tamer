    public void get(InputStream i, OutputStream o) throws Exception {
        byte b[] = new byte[8192];
        int l = read(i, b, 0, 256);
        if (l > 0) {
            String ss = new String(b, 0, l).trim();
            URL url = new URL(ss);
            InputStream ii = null;
            try {
                ii = url.openStream();
                while ((l = ii.read(b)) >= 0) {
                    o.write(b, 0, l);
                }
            } finally {
                if (ii != null) {
                    ii.close();
                }
            }
        }
    }
