    private void testIfNoneMatch(byte[] content) throws Exception {
        String eTag = c.getHeaderField("ETag");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(content);
        String hexDigest = getHexString(digest);
        if (hexDigest.equals(eTag)) System.out.print("eTag content          : md5 hex string");
        String quotedHexDigest = "\"" + hexDigest + "\"";
        if (quotedHexDigest.equals(eTag)) System.out.print("eTag content          : quoted md5 hex string");
        HttpURLConnection c2 = (HttpURLConnection) url.openConnection();
        c2.addRequestProperty("If-None-Match", eTag);
        c2.connect();
        int code = c2.getResponseCode();
        boolean supported = (code == 304);
        ifNoneMatchSupported = supported;
        c2.disconnect();
    }
