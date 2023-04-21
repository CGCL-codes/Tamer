    private void doFrame() {
        logger.debug("WsHybi10#doFrame cid:" + handler.getChannelId());
        byte pcode = frame.getPcode();
        ByteBuffer[] payloadBuffers = frame.getPayloadBuffers();
        doBinaryTrace(pcode, payloadBuffer == null, frame.isFin(), payloadBuffers);
        if (payloadBuffer == null) {
            payloadBuffer = CacheBuffer.open();
        }
        if (!frame.isFin()) {
            logger.debug("WsHybi10#doFrame not isFin");
            if (pcode != WsHybiFrame.PCODE_CONTINUE) {
                continuePcode = pcode;
            }
            continuePayloadLength += BuffersUtil.remaining(payloadBuffers);
            payloadBuffer.putBuffer(payloadBuffers);
            if (continuePayloadLength >= getWebSocketMessageLimit()) {
                logger.debug("WsHybi10#doFrame too long frame.continuePayloadLength:" + continuePayloadLength);
                sendClose(WsHybiFrame.CLOSE_MESSAGE_TOO_BIG, "too long frame");
            }
            return;
        }
        if (pcode == WsHybiFrame.PCODE_CONTINUE) {
            logger.debug("WsHybi10#doFrame pcode CONTINUE");
            pcode = continuePcode;
            continuePayloadLength = 0;
            continuePcode = -1;
        }
        payloadBuffer.putBuffer(payloadBuffers);
        payloadBuffer.flip();
        switch(pcode) {
            case WsHybiFrame.PCODE_TEXT:
                logger.debug("WsHybi10#doFrame pcode TEXT");
                if (!payloadBuffer.isInTopBuffer()) {
                    throw new UnsupportedOperationException("unsuppert big text");
                }
                payloadBuffers = payloadBuffer.popTopBuffer();
                for (ByteBuffer buffer : payloadBuffers) {
                    convertPutBuffer(buffer);
                }
                PoolManager.poolArrayInstance(payloadBuffers);
                String textMessage = convertToString();
                traceOnMessage(textMessage);
                callOnMessage(textMessage);
                break;
            case WsHybiFrame.PCODE_BINARY:
                logger.debug("WsHybi10#doFrame pcode BINARY");
                callBinaryOnMessage(payloadBuffer);
                payloadBuffer = null;
                break;
            case WsHybiFrame.PCODE_CLOSE:
                logger.debug("WsHybi10#doFrame pcode CLOSE");
                PoolManager.poolBufferInstance(payloadBuffers);
                traceOnClose(frame.getCloseCode(), frame.getCloseReason());
                sendClose(WsHybiFrame.CLOSE_NORMAL, null);
                break;
            case WsHybiFrame.PCODE_PING:
                logger.debug("WsHybi10#doFrame pcode PING");
                tracePingPong("PING");
                ByteBuffer[] pongBuffer = WsHybiFrame.createPongFrame(isWebSocketResponseMask(), payloadBuffers);
                handler.asyncWrite(null, pongBuffer);
                break;
            case WsHybiFrame.PCODE_PONG:
                logger.debug("WsHybi10#doFrame pcode PONG");
                PoolManager.poolBufferInstance(payloadBuffers);
                tracePingPong("PONG");
                break;
        }
        if (payloadBuffer != null) {
            payloadBuffer.unref();
            payloadBuffer = null;
        }
        if (frame.parseNextFrame()) {
            doFrame();
        }
    }
