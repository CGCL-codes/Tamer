    public String openURL(URL u) throws UnsupportedEncodingException {
        InputStream is;
        byte[] buf = new byte[1024];
        URLConnection urlc = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            urlc = u.openConnection();
            is = urlc.getInputStream();
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            is.close();
        } catch (IOException e) {
            try {
                ((HttpURLConnection) urlc).getResponseCode();
                InputStream es = ((HttpURLConnection) urlc).getErrorStream();
                int ret = 0;
                while ((ret = es.read(buf)) > 0) {
                }
                es.close();
            } catch (IOException ex) {
            }
        }
        return new String(bos.toByteArray(), "UTF-8");
    }
