    private boolean sendMessagePDU(OutboundMessage msg) throws TimeoutException, GatewayException, IOException, InterruptedException {
        int j, refNo;
        String pdu;
        boolean ok = false;
        atHandler.keepGsmLinkOpen();
        if (!msg.isBig()) {
            pdu = msg.getPDU(smscNumber, 0, 0);
            j = pdu.length();
            j /= 2;
            if (smscNumber == null) ; else if (smscNumber.length() == 0) j--; else {
                int smscNumberLen = smscNumber.length();
                if (smscNumber.charAt(0) == '+') smscNumberLen--;
                if (smscNumberLen % 2 != 0) smscNumberLen++;
                int smscLen = (2 + smscNumberLen) / 2;
                j = j - smscLen - 1;
            }
            refNo = atHandler.sendMessage(j, pdu, null, null);
            if (refNo >= 0) {
                msg.setGatewayId(gtwId);
                msg.setRefNo("" + refNo);
                msg.setDispatchDate(new Date());
                msg.setMessageStatus(MessageStatuses.SENT);
                incOutboundMessageCount();
                ok = true;
            } else {
                msg.setRefNo(null);
                msg.setDispatchDate(null);
                msg.setMessageStatus(MessageStatuses.FAILED);
                ok = false;
            }
        } else {
            for (int partNo = 1; partNo <= msg.getNoOfParts(); partNo++) {
                pdu = msg.getPDU(smscNumber, outMpRefNo, partNo);
                j = pdu.length();
                j /= 2;
                if (smscNumber == null) ; else if (smscNumber.length() == 0) j--; else {
                    int smscNumberLen = smscNumber.length();
                    if (smscNumber.charAt(0) == '+') smscNumberLen--;
                    if (smscNumberLen % 2 != 0) smscNumberLen++;
                    int smscLen = (2 + smscNumberLen) / 2;
                    j = j - smscLen - 1;
                }
                refNo = atHandler.sendMessage(j, pdu, null, null);
                if (refNo >= 0) {
                    msg.setGatewayId(gtwId);
                    msg.setRefNo("" + refNo);
                    msg.setDispatchDate(new Date());
                    msg.setMessageStatus(MessageStatuses.SENT);
                    incOutboundMessageCount();
                    ok = true;
                } else {
                    msg.setRefNo(null);
                    msg.setDispatchDate(null);
                    msg.setMessageStatus(MessageStatuses.FAILED);
                }
            }
            outMpRefNo = (outMpRefNo + 1) % 65536;
        }
        return ok;
    }
