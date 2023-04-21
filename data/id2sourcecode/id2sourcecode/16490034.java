    private URLConnection createConnection(HttpServletRequest req, HttpServletResponse resp, String url) throws Exception {
        URLConnection con;
        con = openConnection(url, req, resp);
        String contentType;
        String pathInfo = req.getPathInfo();
        String mimeType = context.getMimeType(pathInfo);
        if (mimeType != null && !mimeType.equals("application/xhtml+xml")) {
            contentType = mimeType;
        } else {
            contentType = con.getContentType();
        }
        resp.setContentType(contentType);
        long maxAge = 0;
        try {
            long expiration = con.getExpiration();
            maxAge = (expiration - System.currentTimeMillis()) / 1000;
            if (maxAge < 0) maxAge = 0;
        } catch (Exception e) {
        }
        resp.setHeader("Cache-Control", "max-age=" + maxAge);
        return con;
    }
