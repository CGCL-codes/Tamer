    public static String calcResolveDigest(String method, boolean isHttps, String origin, String path, String query) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        if (isHttps) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }
        sb.append(origin);
        sb.append(path);
        if (query != null) {
            sb.append("?");
            sb.append(query);
        }
        String resolveString = sb.toString();
        try {
            logger.debug("calcResolveDigest resolveString:" + resolveString);
            String digest = DataUtil.digest(resolveString.getBytes(HeaderParser.HEADER_ENCODE));
            return digest;
        } catch (UnsupportedEncodingException e) {
            logger.error("getDigest error.", e);
        }
        return null;
    }
