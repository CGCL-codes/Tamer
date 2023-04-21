    public void onRequestEnd(Object userContext, int stat) {
        logger.debug("#webClientEnd.cid:" + getChannelId());
        if (isTryAgain) {
            isTryAgain = false;
            startResponse();
            return;
        }
        if (injector != null) {
            ByteBuffer[] buffers = injector.onResponseBody(null);
            if (buffers != null) {
                logger.debug("inject add contents last cid:" + getChannelId());
                responseBody(buffers);
            }
        }
        if (getResponseStatusCode() == null) {
            completeResponse("500", "no response");
        } else {
            responseEnd();
        }
    }
