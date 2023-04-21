    private void flushFirstResponse(ByteBuffer[] secondBody) {
        setupResponseHeader();
        ByteBuffer[] bodyBuffers = BuffersUtil.concatenate(firstBody, secondBody);
        long commitContentLength = -1;
        if (secondBody == null) {
            bodyBuffers = zipedIfNeed(true, bodyBuffers);
            commitContentLength = BuffersUtil.remaining(bodyBuffers);
            responseHeader.setContentLength(commitContentLength);
        } else {
            bodyBuffers = zipedIfNeed(false, bodyBuffers);
        }
        prepareKeepAlive(commitContentLength);
        ByteBuffer[] headerBuffer = responseHeader.getHeaderBuffer();
        if (headerBuffer == null) {
            logger.warn("flushFirstResponse fail to getHeaderBuffer.cid:" + getChannelId());
            logger.warn("firstBody:" + firstBody + ":secondBody:" + secondBody);
            asyncClose(null);
            return;
        }
        responseHeaderLength = BuffersUtil.remaining(headerBuffer);
        Store responsePeek = null;
        MappingResult mapping = getRequestMapping();
        if (mapping != null) {
            switch(mapping.getLogType()) {
                case RESPONSE_TRACE:
                case TRACE:
                    responsePeek = Store.open(true);
                    ByteBuffer[] headerDup = PoolManager.duplicateBuffers(headerBuffer);
                    responsePeek.putBuffer(headerDup);
                    AccessLog accessLog = getAccessLog();
                    logger.debug("#flushFirstResponse" + responsePeek.getStoreId());
                    accessLog.incTrace();
                    responsePeek.close(accessLog, responsePeek);
                    accessLog.setResponseHeaderDigest(responsePeek.getDigest());
                    responsePeek = Store.open(true);
            }
        }
        getAccessLog().setTimeCheckPint(AccessLog.TimePoint.responseHeader);
        if (firstBody == null && secondBody == null) {
            getAccessLog().setTimeCheckPint(AccessLog.TimePoint.responseBody);
            asyncWrite(WRITE_CONTEXT_LAST_HEADER, headerBuffer);
            if (responsePeek != null) {
                responsePeek.close();
            }
            return;
        }
        firstBody = null;
        logger.debug("flushFirstResponse cid:" + getChannelId() + ":header[0]:" + headerBuffer[0]);
        asyncWrite(WRITE_CONTEXT_HEADER, headerBuffer);
        if (responsePeek != null) {
            pushWritePeekStore(responsePeek);
        }
        if (secondBody == null) {
            internalWriteBody(true, true, bodyBuffers);
        } else {
            internalWriteBody(false, true, bodyBuffers);
        }
    }
