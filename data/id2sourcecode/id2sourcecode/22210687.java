    public static byte[] downloadHTTP(String url, StringBuffer contentType, StringBuffer newUrl, StringBuffer encoding) throws Exception {
        if (contentType == null) contentType = new StringBuffer();
        if (newUrl == null) newUrl = new StringBuffer();
        if (encoding == null) encoding = new StringBuffer();
        URL src = new URL(url);
        URLConnection urlConn = src.openConnection();
        urlConn.setReadTimeout(10 * 1000);
        urlConn.setConnectTimeout(10 * 1000);
        urlConn.setDoInput(true);
        urlConn.setDoOutput(false);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("User-Agent", "mle|0.8.9|Nokia6680|editor");
        urlConn.setRequestProperty("Accept-Encoding", "UTF-8");
        int len = urlConn.getContentLength();
        BufferedInputStream in = new BufferedInputStream(urlConn.getInputStream());
        contentType.append(urlConn.getContentType());
        String ct = urlConn.getHeaderField("Content-Type");
        newUrl.append(urlConn.getURL().toString());
        if (ct != null && ct.indexOf("charset=") > 0) {
            int a = ct.indexOf("charset=") + "charset=".length();
            encoding.append(ct.substring(a));
        }
        System.out.println("encoding:" + encoding);
        byte[] data = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int b = 0;
        while ((b = in.read()) != -1) {
            bout.write(b);
        }
        data = bout.toByteArray();
        in.close();
        if (encoding.length() <= 0) {
            if (HelperStd.checkUTFEncoding(data) > 0) encoding.append("UTF-8"); else encoding.append("ISO-8859-1");
        }
        System.out.println("encoding:" + encoding);
        return data;
    }
