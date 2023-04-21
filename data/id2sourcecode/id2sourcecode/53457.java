    public boolean onEvent(SKJMessage msg) {
        boolean result = false;
        logger.finest("CallRecord::onEvent() got a message!");
        if (msg instanceof SK_RequestChannelAck) {
            SK_RequestChannelAck ack = (SK_RequestChannelAck) msg;
            int span = ack.getSpan();
            int channel = ack.getChannel();
            if (aLeg == null) {
                logger.finest("Request A Leg Channel ACK: Status " + SKJava.statusText(ack.getSKStatus()) + ", Port: " + span + ":" + channel);
                if (ack.getSKStatus() == 0) {
                    Integer hashKey = new Integer((span * 100) + channel);
                    aLeg = new Leg(ss, csp, this, span, channel, 'A');
                    ss.channelLeg.put(hashKey, aLeg);
                    XL_OutseizeControl oc = new XL_OutseizeControl();
                    oc.setICBCount(3);
                    byte[] data = new byte[1000];
                    int n = 0;
                    data[n++] = 1;
                    data[n++] = 0x0a;
                    data[n++] = 0;
                    data[n++] = 1;
                    data[n++] = 0x0b;
                    data[n++] = 1;
                    data[n++] = 8;
                    String callDigits = "123457890";
                    int numDigits = callDigits.length() + 1;
                    int numBytes = (numDigits + numDigits % 2) / 2;
                    data[n++] = 2;
                    data[n++] = 1;
                    data[n++] = (byte) (4 + 1 + numBytes);
                    data[n++] = 1;
                    data[n++] = 1;
                    data[n++] = 1;
                    data[n++] = 1;
                    data[n++] = (byte) numDigits;
                    for (int i = 0; i < numBytes; i++) {
                        int dgt1;
                        int dgt2;
                        if (callDigits.length() == i * 2) {
                            dgt1 = 0xf;
                            dgt2 = 0;
                        } else if (callDigits.length() == i * 2 + 1) {
                            dgt1 = callDigits.charAt(i * 2) - '0';
                            dgt2 = 0xf;
                        } else {
                            dgt1 = callDigits.charAt(i * 2) - '0';
                            dgt2 = callDigits.charAt(i * 2 + 1) - '0';
                        }
                        data[n++] = (byte) ((dgt1 << 4) | dgt2);
                    }
                    byte[] tempData = new byte[n];
                    System.arraycopy(data, 0, tempData, 0, n);
                    oc.setICBData(tempData);
                    logger.finest("Sending B Leg outseize request");
                    csp.requestOutseizedChannel("TRITON_0", 0, oc, this);
                } else {
                    logger.severe("A Leg call FAILED.  Status: " + SKJava.statusText(ack.getSKStatus()));
                    endCall();
                }
            } else {
                logger.finest("Request B Leg Channel ACK: Status " + SKJava.statusText(ack.getSKStatus()) + ", Port: " + span + ":" + channel);
                if (ack.getSKStatus() == 0) {
                    Integer hashKey = new Integer((span * 100) + channel);
                    bLeg = new Leg(ss, csp, this, span, channel, 'B');
                    ss.channelLeg.put(hashKey, bLeg);
                    aLeg.connect(bLeg.getSpan(), bLeg.getChannel());
                    releaseTimer = new TerminateTimer(this);
                    new Timer().schedule(releaseTimer, ss.callDuration);
                } else {
                    logger.severe("B Leg call FAILED.  Status: " + SKJava.statusText(ack.getSKStatus()));
                    {
                        aLeg.release();
                        aLeg = null;
                        ss.activeCallCount--;
                    }
                }
            }
            result = true;
        }
        return result;
    }
