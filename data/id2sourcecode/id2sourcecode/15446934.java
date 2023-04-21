    public void onResponseHeader(Object userContext, HeaderParser responseHeader) {
        logger.debug("#responseHeader.cid:" + getChannelId());
        long injectLength = 0;
        boolean isInject = false;
        if (injector != null) {
            injector.onResponseHeader(responseHeader);
            isInject = injector.isInject();
            if (isInject) {
                webClientHandler.setReadableCallback(true);
                injectLength = injector.getInjectLength();
                logger.debug("inject cid:" + getChannelId() + ":injectLength:" + injectLength + ":ContentsLength:" + responseHeader.getContentLength());
            }
        }
        String statusCode = responseHeader.getStatusCode();
        if ("301".equals(statusCode) || "302".equals(statusCode) || "303".equals(statusCode)) {
            rewriteLocation(responseHeader);
        }
        setResponseHeader(responseHeader);
        for (String removeHeader : removeResponseHeaders) {
            removeHeader(removeHeader);
        }
        for (String name : addResponseHeaders.keySet()) {
            String value = addResponseHeaders.get(name);
            setHeader(name, value);
        }
        if (isInject) {
            String contentEncoding = responseHeader.getHeader(HeaderParser.CONTENT_ENCODING_HEADER);
            long contentLength = 0;
            if (!isReplace) {
                contentLength = responseHeader.getContentLength();
            }
            if (contentEncoding != null) {
                removeHeader(HeaderParser.CONTENT_ENCODING_HEADER);
                removeContentLength();
            } else if (contentLength >= 0) {
                setContentLength(contentLength + injectLength);
                logger.debug("inject change contentLength cid:" + getChannelId() + ":contentLength:" + (contentLength + injectLength));
            }
            removeHeader(HeaderParser.TRANSFER_ENCODING_HEADER);
        }
    }
