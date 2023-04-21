    private void doFrame() {
        logger.debug("WsClientHandler#doFrame cid:" + getChannelId());
        byte pcode = frame.getPcode();
        ByteBuffer[] payloadBuffers = frame.getPayloadBuffers();
        if (!frame.isFin()) {
            logger.debug("WsClientHandler#doFrame not isFin");
            if (pcode != WsHybiFrame.PCODE_CONTINUE) {
                continuePcode = pcode;
            }
            for (ByteBuffer buffer : payloadBuffers) {
                continuePayload.add(buffer);
                continuePayloadLength += buffer.remaining();
            }
            PoolManager.poolArrayInstance(payloadBuffers);
            if (continuePayloadLength >= webSocketMessageLimit) {
                logger.debug("WsClientHandler#doFrame too long frame.continuePayloadLength:" + continuePayloadLength);
                sendClose(WsHybiFrame.CLOSE_MESSAGE_TOO_BIG, "too long frame");
            }
            return;
        }
        if (pcode == WsHybiFrame.PCODE_CONTINUE) {
            logger.debug("WsClientHandler#doFrame pcode CONTINUE");
            pcode = continuePcode;
            for (ByteBuffer buffer : payloadBuffers) {
                continuePayload.add(buffer);
            }
            PoolManager.poolArrayInstance(payloadBuffers);
            int size = continuePayload.size();
            payloadBuffers = BuffersUtil.newByteBufferArray(size);
            for (int i = 0; i < size; i++) {
                payloadBuffers[i] = continuePayload.get(i);
            }
            continuePayload.clear();
            continuePayloadLength = 0;
            continuePcode = -1;
        }
        switch(pcode) {
            case WsHybiFrame.PCODE_TEXT:
                logger.debug("WsClientHandler#doFrame pcode TEXT");
                for (ByteBuffer buffer : payloadBuffers) {
                    codeConverte.putBuffer(buffer);
                }
                PoolManager.poolArrayInstance(payloadBuffers);
                try {
                    onWcMessage(codeConverte.convertToString());
                } catch (IOException e) {
                    logger.error("codeConvert error.", e);
                    sendClose(WsHybiFrame.CLOSE_INVALID_FRAME, "invalid frame");
                    throw new RuntimeException("codeConvert error.");
                }
                break;
            case WsHybiFrame.PCODE_BINARY:
                logger.debug("WsClientHandler#doFrame pcode BINARY");
                onWcMessage(CacheBuffer.open(payloadBuffers));
                break;
            case WsHybiFrame.PCODE_CLOSE:
                logger.debug("WsClientHandler#doFrame pcode CLOSE");
                PoolManager.poolBufferInstance(payloadBuffers);
                sendClose(WsHybiFrame.CLOSE_NORMAL, "OK");
                doEndWsClient(frame.getCloseCode(), frame.getCloseReason());
                break;
            case WsHybiFrame.PCODE_PING:
                logger.debug("WsClientHandler#doFrame pcode PING");
                ByteBuffer[] pongBuffer = WsHybiFrame.createPongFrame(true, payloadBuffers);
                asyncWrite(null, pongBuffer);
                break;
            case WsHybiFrame.PCODE_PONG:
                logger.debug("WsClientHandler#doFrame pcode PONG");
                PoolManager.poolBufferInstance(payloadBuffers);
                break;
        }
        if (frame.parseNextFrame()) {
            doFrame();
        }
    }
