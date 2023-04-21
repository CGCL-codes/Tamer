    public void handleRequest(HttpServiceContext context) throws Exception {
        DefaultHttpResponse response = new DefaultHttpResponse();
        String path = context.getRequest().getRequestUri().getPath().substring(1);
        URL url = new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), baseUrl.getPath() + path);
        if (!url.getPath().startsWith(baseUrl.getPath())) {
            response.setStatus(NOT_FOUND);
            context.commitResponse(response);
            return;
        }
        URLConnection connection = url.openConnection();
        long lastModified = connection.getLastModified();
        if (lastModified != 0 && lastModified <= ifModifiedSince(context.getRequest())) {
            response.setStatus(NOT_MODIFIED);
        } else {
            if (lastModified != 0) {
                response.setHeader("Last-Modified", formatDate(lastModified));
                response.setHeader("Expires", formatDate(currentTimeMillis() + MAX_CACHE_AGE));
            }
            String contentType = guessContentType(path);
            if (contentType != null) response.setHeader("Content-Type", contentType);
            try {
                response.setContent(contentsOf(connection));
                response.setStatus(OK);
            } catch (FileNotFoundException ex) {
                response.setStatus(NOT_FOUND);
            }
        }
        context.commitResponse(response);
    }
