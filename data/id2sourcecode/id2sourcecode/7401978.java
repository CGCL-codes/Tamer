        private void readHybi(ByteArrayOutputStream aBuff, WebSocketEngine aEngine) throws IOException {
            int lPacketType;
            DataInputStream lDis = new DataInputStream(mIn);
            while (mIsRunning) {
                try {
                    int lFlags = lDis.read();
                    boolean lFragmented = (0x01 & lFlags) == 0x01;
                    int lType = lFlags >> 4;
                    lPacketType = WebSocketProtocolHandler.toRawPacketType(lType);
                    if (lPacketType == -1) {
                        mLog.trace("Dropping packet with unknown type: " + lType);
                    } else {
                        long lPayloadLen = mIn.read() >> 1;
                        if (lPayloadLen == 126) {
                            lPayloadLen = lDis.readUnsignedShort();
                        } else if (lPayloadLen == 127) {
                            lPayloadLen = lDis.readLong();
                        }
                        if (lPayloadLen > 0) {
                            while (lPayloadLen-- > 0) {
                                aBuff.write(lDis.read());
                            }
                        }
                        if (!lFragmented) {
                            if (lPacketType == RawPacket.FRAMETYPE_PING) {
                                WebSocketPacket lPong = new RawPacket(aBuff.toByteArray());
                                lPong.setFrameType(RawPacket.FRAMETYPE_PONG);
                                sendPacket(lPong);
                            } else if (lPacketType == RawPacket.FRAMETYPE_CLOSE) {
                                mCloseReason = CloseReason.CLIENT;
                                mIsRunning = false;
                                WebSocketPacket lClose = new RawPacket(aBuff.toByteArray());
                                lClose.setFrameType(RawPacket.FRAMETYPE_CLOSE);
                                sendPacket(lClose);
                            }
                            WebSocketPacket lPacket = new RawPacket(aBuff.toByteArray());
                            lPacket.setFrameType(lPacketType);
                            try {
                                aEngine.processPacket(mConnector, lPacket);
                            } catch (Exception lEx) {
                                mLog.error(lEx.getClass().getSimpleName() + " in processPacket of connector " + mConnector.getClass().getSimpleName() + ": " + lEx.getMessage());
                            }
                            aBuff.reset();
                        }
                    }
                } catch (SocketTimeoutException lEx) {
                    mLog.error("(timeout) " + lEx.getClass().getSimpleName() + ": " + lEx.getMessage());
                    mCloseReason = CloseReason.TIMEOUT;
                    mIsRunning = false;
                } catch (Exception lEx) {
                    mLog.error("(other) " + lEx.getClass().getSimpleName() + ": " + lEx.getMessage());
                    mCloseReason = CloseReason.SERVER;
                    mIsRunning = false;
                }
            }
        }
