    private void handleChannelRequest(SMQOMPackage pack) {
        try {
            if (pack.getDataSize() > 0) {
                String channelName = new String(pack.getDataBytes());
                clientChannel = MBController.getChannel(channelName);
                if (clientChannel != null) {
                    logger.trace("Granting channel access to client.");
                    logger.trace("Client Handler " + this.threadId + ": Channel " + channelName + " found!");
                    sendPackage(SMQOMCommandId.SND_CHANNEL_ACK, null);
                    SMQOMPackage pkg = SMQOMProtocol.readPackageFromStream(input);
                    if (SMQOMProtocol.isAcknowledgement(pkg)) {
                        logger.trace("Client Handler " + this.threadId + ": Client has accepted channel's granting");
                    }
                } else {
                    output.write(new SMQOMPackage(SMQOMCommandId.SND_REQ_DENY, sessionId, null).getBytes());
                    output.flush();
                    SMQOMPackage pkg = SMQOMProtocol.readPackageFromStream(input);
                    if (SMQOMProtocol.isAcknowledgement(pkg)) {
                        logger.trace("Client Handler " + this.threadId + ": Client has accepted channel granting's denial.");
                    }
                }
            } else {
                output.write(new SMQOMPackage(SMQOMCommandId.SND_REQ_DENY, sessionId, null).getBytes());
                output.flush();
                SMQOMPackage pkg = SMQOMProtocol.readPackageFromStream(input);
                if (SMQOMProtocol.isAcknowledgement(pkg)) {
                    logger.trace("Client Handler " + this.threadId + ": Client has accepted channel granting's denial.");
                }
            }
        } catch (IOException e) {
            logger.error("IOException when handling client disconnect request", e);
        }
    }
