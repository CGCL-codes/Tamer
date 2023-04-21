    public boolean onEvent(SKJMessage msg) {
        boolean result = false;
        logger.finest("Leg::onEvent() got a message!");
        if (msg instanceof XL_ChannelReleasedWithData) {
            XL_ChannelReleasedWithData crwd = (XL_ChannelReleasedWithData) msg;
            if ((crwd.getSpan() == span) && (crwd.getChannel() == channel)) {
                logger.finest("Leg RELEASED");
                Integer hashKey = new Integer((span * 100) + channel);
                ss.channelLeg.remove(hashKey);
                span = -1;
                channel = -1;
                result = true;
            }
        } else if (msg instanceof XL_CallProcessingEvent) {
            XL_CallProcessingEvent cpe = (XL_CallProcessingEvent) msg;
            logger.finest("Leg received a Call Processing Event");
            if ((cpe.getSpan() == span) && (cpe.getChannel() == channel)) {
                switch(cpe.getEvent()) {
                    case 0x20:
                        {
                            logger.finest("Leg ANSWERED");
                            if (type == 'A') {
                                logger.finest("Starting digit collection on A Leg");
                                XL_ParkChannel pc = new XL_ParkChannel();
                                pc.setSpanA(span);
                                pc.setChannelA(channel);
                                pc.setSpanB(span);
                                pc.setChannelB(channel);
                                csp.sendMessage(pc, this);
                                XL_CollectDigitString cds = new XL_CollectDigitString();
                                cds.setSpan(span);
                                cds.setChannel(channel);
                                cds.setMode(0x04);
                                cds.setMaxDigits(10);
                                cds.setNumTermChars(1);
                                cds.setConfigBits(0x20);
                                cds.setTermChars(0xf000);
                                cds.setInterDigitTimer(0x01f4);
                                cds.setFirstDigitTimer(0x0bb8);
                                cds.setCompletionTimer(0x0bb8);
                                cds.setMinReceiveDigitDuration(3);
                                cds.setAddressSignallingType(0x01);
                                cds.setNumDigitStrings(1);
                                cds.setResumeDigitCltnTimer(1);
                                csp.sendMessage(cds, this);
                                XL_RecAnnConnect rac = new XL_RecAnnConnect();
                                rac.setSpan(span);
                                rac.setChannel(channel);
                                rac.setConfig(0x00);
                                rac.setEvent(0x03);
                                rac.setCnt(1);
                                rac.setID1(0x6f);
                                csp.sendMessage(rac, this);
                            } else {
                                logger.finest("Starting prompts on B Leg");
                                XL_RecAnnConnect rac = new XL_RecAnnConnect();
                                rac.setSpan(span);
                                rac.setChannel(channel);
                                rac.setConfig(0x00);
                                rac.setEvent(0x03);
                                rac.setCnt(2);
                                rac.setID1(0x5e);
                                rac.setID2(0x5f);
                                csp.sendMessage(rac, this);
                            }
                            break;
                        }
                    case 0x25:
                        {
                            logger.finest("Prompt started");
                            break;
                        }
                    case 0x26:
                        {
                            logger.finest("Prompt completed");
                            break;
                        }
                    case 0x02:
                        {
                            byte[] digitData = cpe.getData();
                            int digitLength = digitData[4];
                            String digits = SKJMessage.printableFormat(digitData).substring(4 * 3 + 1, 4 * 3 + 1 + digitLength * 3).replaceAll(":", "");
                            logger.finest("Received a digit string: " + digits + " of length:" + digitLength);
                            break;
                        }
                    default:
                        {
                            logger.warning("Leg received unknown event of: " + cpe.getEvent());
                            break;
                        }
                }
                result = true;
            }
        } else if (msg instanceof XL_ReleaseChannelAck) {
            XL_ReleaseChannelAck ack = (XL_ReleaseChannelAck) msg;
            logger.finest("Leg::onEvent: received ReleaseChannel Ack with status: " + SKJava.statusText(ack.getStatus()));
            result = true;
        } else if (msg instanceof XL_ConnectAck) {
            XL_ConnectAck ack = (XL_ConnectAck) msg;
            logger.finest("Leg::onEvent: received Connect Ack with status: " + SKJava.statusText(ack.getStatus()));
            result = true;
        } else if (msg instanceof XL_ParkChannelAck) {
            XL_ParkChannelAck ack = (XL_ParkChannelAck) msg;
            logger.finest("Leg::onEvent: received Park Channel Ack with status: " + SKJava.statusText(ack.getStatus()));
            result = true;
        } else if (msg instanceof XL_CollectDigitStringAck) {
            XL_CollectDigitStringAck ack = (XL_CollectDigitStringAck) msg;
            logger.finest("Leg::onEvent: received Collect Digit String Ack with status: " + SKJava.statusText(ack.getStatus()));
            result = true;
        } else if (msg instanceof XL_RecAnnConnectAck) {
            XL_RecAnnConnectAck ack = (XL_RecAnnConnectAck) msg;
            logger.finest("Leg::onEvent: received Recorded Announcement Connect Ack with status: " + SKJava.statusText(ack.getStatus()));
            result = true;
        }
        return result;
    }
