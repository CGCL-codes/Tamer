    public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
        logger.debug("#readPlain.cid:" + getChannelId());
        if (userContext == CONTEXT_BODY) {
            stat = STAT_RESPONSE_BODY;
            boolean isLast;
            if (isReadableCallback) {
                buffers = responseChunk.decodeChunk(buffers);
                isLast = responseChunk.isEndOfData();
                if (isGzip) {
                    gzipContext.putZipedBuffer(buffers);
                    buffers = gzipContext.getPlainBuffer();
                }
            } else {
                isLast = responseChunk.isEndOfData(buffers);
            }
            onResponseBody(buffers);
            if (isLast) {
                endOfResponse();
            } else {
                logger.debug("asyncRead(CONTEXT_BODY) cid:" + getChannelId());
                asyncRead(CONTEXT_BODY);
            }
            return;
        }
        stat = STAT_RESPONSE_HEADER;
        for (int i = 0; i < buffers.length; i++) {
            responseHeader.parse(buffers[i]);
        }
        PoolManager.poolArrayInstance(buffers);
        if (!responseHeader.isParseEnd()) {
            logger.debug("asyncRead(CONTEXT_HEADER) cid:" + getChannelId());
            asyncRead(CONTEXT_HEADER);
            return;
        }
        if (responseHeader.isParseError()) {
            logger.warn("http header error");
            asyncClose(null);
            return;
        }
        responseHeaderLength = responseHeader.getHeaderLength();
        String statusCode = responseHeader.getStatusCode();
        String transfer = responseHeader.getHeader(HeaderParser.TRANSFER_ENCODING_HEADER);
        String encoding = responseHeader.getHeader(HeaderParser.CONTENT_ENCODING_HEADER);
        isGzip = false;
        if (HeaderParser.CONTENT_ENCODING_GZIP.equalsIgnoreCase(encoding)) {
            isGzip = true;
        }
        onResponseHeader(responseHeader);
        if ("304".equals(statusCode) || "204".equals(statusCode)) {
            endOfResponse();
            return;
        }
        long responseContentLength = responseHeader.getContentLength();
        if (responseContentLength < 0) {
            responseContentLength = Long.MAX_VALUE;
        }
        boolean isChunked = HeaderParser.TRANSFER_ENCODING_CHUNKED.equalsIgnoreCase(transfer);
        ByteBuffer[] body = responseHeader.getBodyBuffer();
        responseChunk.decodeInit(isChunked, responseContentLength);
        boolean isLast;
        if (isReadableCallback) {
            body = responseChunk.decodeChunk(body);
            isLast = responseChunk.isEndOfData();
            if (isGzip) {
                gzipContext.putZipedBuffer(body);
                body = gzipContext.getPlainBuffer();
            }
        } else {
            isLast = responseChunk.isEndOfData(body);
        }
        if (body != null) {
            stat = STAT_RESPONSE_BODY;
            onResponseBody(body);
        }
        if (isLast) {
            onResponseBody(null);
            endOfResponse();
        } else {
            logger.debug("asyncRead(CONTEXT_BODY) cid:" + getChannelId());
            asyncRead(CONTEXT_BODY);
        }
    }
