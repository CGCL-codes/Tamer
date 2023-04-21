    public boolean perform() throws IOException {
        URLConnection urlConn;
        StringBuffer sb;
        InputStream is;
        byte[] bytes;
        int len;
        URL url;
        url = new URL(u);
        urlConn = url.openConnection();
        urlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
        urlConn.setConnectTimeout(7000);
        is = urlConn.getInputStream();
        sb = new StringBuffer();
        bytes = new byte[4096];
        while ((len = is.read(bytes)) > 0) {
            sb.append(new String(bytes, 0, len));
            Arrays.fill(bytes, (byte) 0);
        }
        is.close();
        return decode(sb.toString().getBytes());
    }
