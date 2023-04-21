    public static CDF getCDF(URL url) throws IOException {
        URLConnection con = url.openConnection();
        int remaining = con.getContentLength();
        InputStream is = con.getInputStream();
        byte[] ba = new byte[remaining];
        int offset = 0;
        while (remaining > 0) {
            int got = is.read(ba, offset, remaining);
            offset += got;
            remaining -= got;
        }
        return getCDF(ba);
    }
