    private boolean writeResource(final HttpServletRequest req, final HttpServletResponse resp, final String resourcePath) throws IOException {
        ServletContext servletContext = config.getServletContext();
        URL url = servletContext.getResource(resourcePath);
        if (url == null) return false;
        URLConnection connection = url.openConnection();
        long lastModified = connection.getLastModified();
        int contentLength = connection.getContentLength();
        String etag = null;
        if (lastModified != -1 && contentLength != -1) etag = "W/\"" + contentLength + "-" + lastModified + "\"";
        String ifNoneMatch = req.getHeader(IF_NONE_MATCH);
        if (ifNoneMatch != null && etag != null && ifNoneMatch.indexOf(etag) != -1) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return true;
        } else {
            long ifModifiedSince = req.getDateHeader(IF_MODIFIED_SINCE);
            if (ifModifiedSince > -1 && lastModified > 0 && lastModified <= (ifModifiedSince + 999)) {
                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return true;
            }
        }
        if (contentLength != -1) resp.setContentLength(contentLength);
        String contentType = servletContext.getMimeType(resourcePath);
        if (contentType == null) contentType = servletContext.getMimeType(resourcePath);
        if (contentType != null) resp.setContentType(contentType);
        if (lastModified > 0) resp.setDateHeader(LAST_MODIFIED, lastModified);
        if (etag != null) resp.setHeader(ETAG, etag);
        InputStream is = null;
        try {
            is = connection.getInputStream();
            OutputStream os = resp.getOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead = is.read(buffer);
            int writtenContentLength = 0;
            while (bytesRead != -1) {
                os.write(buffer, 0, bytesRead);
                writtenContentLength += bytesRead;
                bytesRead = is.read(buffer);
            }
            if (contentLength == -1 || contentLength != writtenContentLength) resp.setContentLength(writtenContentLength);
        } finally {
            if (is != null) is.close();
        }
        return true;
    }
