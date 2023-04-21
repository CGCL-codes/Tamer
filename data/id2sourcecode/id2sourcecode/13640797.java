    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object obj) throws Exception {
        RTMP rtmp = (RTMP) session.getAttribute(ProtocolState.SESSION_KEY);
        if (session.containsAttribute(RTMPConnection.RTMP_HANDSHAKE)) {
            log.trace("Handshake exists on the session");
            RTMPHandshake handshake = (RTMPHandshake) session.getAttribute(RTMPConnection.RTMP_HANDSHAKE);
            int handshakeType = handshake.getHandshakeType();
            if (handshakeType == 0) {
                log.trace("Handshake type is not currently set");
                byte handshakeByte = RTMPConnection.RTMP_NON_ENCRYPTED;
                if (obj instanceof IoBuffer) {
                    IoBuffer message = (IoBuffer) obj;
                    message.mark();
                    handshakeByte = message.get();
                    message.reset();
                }
                handshake.setHandshakeType(handshakeByte);
                rtmp.setEncrypted(handshakeByte == RTMPConnection.RTMP_ENCRYPTED ? true : false);
            } else if (handshakeType == 3) {
                if (rtmp.getState() == RTMP.STATE_CONNECTED) {
                    log.debug("In connected state");
                    session.removeAttribute(RTMPConnection.RTMP_HANDSHAKE);
                    log.debug("Using non-encrypted communications");
                }
            } else if (handshakeType == 6) {
                RTMPMinaConnection conn = (RTMPMinaConnection) session.getAttribute(RTMPConnection.RTMP_CONNECTION_KEY);
                long readBytesCount = conn.getReadBytes();
                long writeBytesCount = conn.getWrittenBytes();
                log.trace("Bytes read: {} written: {}", readBytesCount, writeBytesCount);
                if (writeBytesCount >= (Constants.HANDSHAKE_SIZE * 2)) {
                    log.debug("Assumed to be in a connected state");
                    session.removeAttribute(RTMPConnection.RTMP_HANDSHAKE);
                    log.debug("Using encrypted communications");
                    if (session.containsAttribute(RTMPConnection.RTMPE_CIPHER_IN)) {
                        log.debug("Ciphers already exist on the session");
                    } else {
                        log.debug("Adding ciphers to the session");
                        session.setAttribute(RTMPConnection.RTMPE_CIPHER_IN, handshake.getCipherIn());
                        session.setAttribute(RTMPConnection.RTMPE_CIPHER_OUT, handshake.getCipherOut());
                    }
                }
            }
        }
        Cipher cipher = (Cipher) session.getAttribute(RTMPConnection.RTMPE_CIPHER_IN);
        if (cipher != null) {
            IoBuffer message = (IoBuffer) obj;
            if (rtmp.getState() == RTMP.STATE_HANDSHAKE) {
                byte[] handshakeReply = new byte[Constants.HANDSHAKE_SIZE];
                message.get(handshakeReply);
                rtmp.setState(RTMP.STATE_CONNECTED);
            }
            log.debug("Decrypting buffer: {}", message);
            byte[] encrypted = new byte[message.remaining()];
            message.get(encrypted);
            message.clear();
            message.free();
            byte[] plain = cipher.update(encrypted);
            IoBuffer messageDecrypted = IoBuffer.wrap(plain);
            log.debug("Decrypted buffer: {}", messageDecrypted);
            nextFilter.messageReceived(session, messageDecrypted);
        } else {
            log.trace("Not decrypting message received: {}", obj);
            nextFilter.messageReceived(session, obj);
        }
    }
