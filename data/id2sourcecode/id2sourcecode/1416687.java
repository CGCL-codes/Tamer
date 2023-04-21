    public <T> T doRequest(WmsRequest<T> req) throws IOException {
        URL url = makeUrl(req);
        if (log.isDebugEnabled()) log.debug("doRequest(" + url + ")");
        System.out.println("doRequest(" + url + ")");
        URLConnection uConn = url.openConnection();
        System.out.println("Default read timeout:" + uConn.getReadTimeout() + "  Connect timeout:" + uConn.getConnectTimeout());
        uConn.setReadTimeout(30000);
        uConn.setConnectTimeout(30000);
        uConn.connect();
        InputStream in = uConn.getInputStream();
        try {
            Map<String, List<String>> header = uConn.getHeaderFields();
            if (log.isDebugEnabled()) log.debug("Header:" + header);
            String contentType = uConn.getContentType();
            if (contentType == null) {
                log.warn("Could not retrieve content type from response for:" + url);
                log.warn("Headers:" + header);
                String test = URLConnection.guessContentTypeFromStream(in);
                log.warn("Content guess:" + test);
            }
            String charset = "UTF-8";
            int split = contentType == null ? -1 : contentType.indexOf(";");
            if (split > 0) {
                String extra = contentType.substring(split + 1);
                contentType = contentType.substring(0, split);
                if (extra.startsWith("charset=")) charset = extra.substring("charset=".length());
            }
            if (log.isDebugEnabled()) log.debug("Content type:" + contentType);
            if (TYPE_SERVICE_EXCEPTION.equals(contentType)) {
                String error = StringUtils.readString(new InputStreamReader(in, charset));
                throw new RuntimeException(error);
            }
            T response = req.readResponse(in);
            return response;
        } finally {
            in.close();
        }
    }
