    public static final byte[] getBytesFromUrl(String urlString) {
        byte[] result = null;
        try {
            URL url = new URL(urlString);
            URLConnection urlc = url.openConnection();
            urlc.setConnectTimeout(5000);
            urlc.setReadTimeout(5000);
            InputStream ins = urlc.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(120 * 1024);
            byte[] buf = new byte[1024];
            while (true) {
                int n = ins.read(buf);
                if (n == -1) {
                    break;
                }
                bout.write(buf, 0, n);
            }
            result = bout.toByteArray();
            bout.close();
            bout = null;
            ins = null;
        } catch (Exception e) {
            Generic.debug("Problem downloading AGPS data");
        }
        return result;
    }
