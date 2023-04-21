    public final void responseBody(ByteBuffer[] buffers) {
        responseWriteBodyApl += BuffersUtil.remaining(buffers);
        boolean isCallbackOnWrittenBody = false;
        synchronized (this) {
            if (getChannelId() == -1) {
                return;
            }
            if (isFlushFirstResponse == false && firstBody != null) {
                flushFirstResponse(buffers);
                isFlushFirstResponse = true;
                return;
            } else if (isFlushFirstResponse == false && firstBody == null) {
                firstBody = buffers;
                isCallbackOnWrittenBody = true;
            }
        }
        if (isCallbackOnWrittenBody) {
            onWrittenBody();
            return;
        }
        if (isFlushFirstResponse) {
            buffers = zipedIfNeed(false, buffers);
            if (buffers == null) {
                onWrittenBody();
            } else {
                internalWriteBody(false, true, buffers);
            }
        }
        if (needMoreResponse()) {
            return;
        }
        responseEnd();
    }
