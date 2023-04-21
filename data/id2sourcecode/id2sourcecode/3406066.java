        private void readHybi() throws WebSocketException, IOException {
            int lPacketType;
            DataInputStream lDis = new DataInputStream(mIS);
            ByteArrayOutputStream lBuff = new ByteArrayOutputStream();
            while (!mStop) {
                int lFlags = lDis.read();
                boolean lFragmented = (0x01 & lFlags) == 0x01;
                int lType = lFlags >> 4;
                lPacketType = WebSocketProtocolHandler.toRawPacketType(lType);
                if (lPacketType == -1) {
                    handleError();
                } else {
                    long lPayloadLen = mIS.read() >> 1;
                    if (lPayloadLen == 126) {
                        lPayloadLen = lDis.readUnsignedShort();
                    } else if (lPayloadLen == 127) {
                        lPayloadLen = lDis.readLong();
                    }
                    if (lPayloadLen > 0) {
                        while (lPayloadLen-- > 0) {
                            lBuff.write(lDis.read());
                        }
                    }
                    if (!lFragmented) {
                        if (lPacketType == RawPacket.FRAMETYPE_PING) {
                            WebSocketPacket lPong = new RawPacket(lBuff.toByteArray());
                            lPong.setFrameType(RawPacket.FRAMETYPE_PONG);
                            send(lPong);
                        } else if (lPacketType == RawPacket.FRAMETYPE_CLOSE) {
                            close();
                        }
                        WebSocketPacket lPacket = new RawPacket(lBuff.toByteArray());
                        lPacket.setFrameType(lPacketType);
                        WebSocketClientEvent lWSCE = new WebSocketClientTokenEvent();
                        notifyPacket(lWSCE, lPacket);
                        lBuff.reset();
                    }
                }
            }
        }
