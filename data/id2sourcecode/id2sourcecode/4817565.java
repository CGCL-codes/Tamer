        private void parseService(KNXnetIPHeader h, byte[] data, int offset, InetAddress src, int port) throws KNXFormatException, IOException {
            final int svc = h.getServiceType();
            if (svc == KNXnetIPHeader.CONNECT_REQ) logger.warn("received connect request - ignored"); else if (svc == KNXnetIPHeader.CONNECT_RES) {
                int newState = OK;
                final ConnectResponse res = new ConnectResponse(data, offset);
                final HPAI ep = res.getDataEndpoint();
                if (res.getStatus() == ErrorCodes.NO_ERROR && ep.getHostProtocol() == HPAI.IPV4_UDP) {
                    channelID = res.getChannelID();
                    final InetAddress ip = ep.getAddress();
                    if (isNatAware && (ip == null || ip.isAnyLocalAddress() || ep.getPort() == 0)) {
                        dataEP = new InetSocketAddress(src, port);
                        logger.info("NAT aware mode: using data endpoint " + dataEP);
                    } else {
                        dataEP = new InetSocketAddress(ip, ep.getPort());
                        logger.info("using assigned data endpoint " + dataEP);
                    }
                    checkVersion(h);
                } else {
                    newState = ACK_ERROR;
                    if (ep != null && ep.getHostProtocol() != HPAI.IPV4_UDP) logger.error("only connection support for UDP/IP"); else logger.error(res.getStatusString());
                }
                setStateNotify(newState);
            } else if (svc == KNXnetIPHeader.CONNECTIONSTATE_REQ) logger.warn("received connection state request - ignored"); else if (svc == KNXnetIPHeader.CONNECTIONSTATE_RES) {
                if (checkVersion(h)) heartbeat.setResponse(new ConnectionstateResponse(data, offset));
            } else if (svc == KNXnetIPHeader.DISCONNECT_REQ) {
                if (ctrlEP.getAddress().equals(src) && ctrlEP.getPort() == port) disconnectRequested(new DisconnectRequest(data, offset));
            } else if (svc == KNXnetIPHeader.DISCONNECT_RES) {
                final DisconnectResponse res = new DisconnectResponse(data, offset);
                if (res.getStatus() != ErrorCodes.NO_ERROR) logger.warn("received disconnect response status 0x" + Integer.toHexString(res.getStatus()) + " (" + ErrorCodes.getErrorMessage(res.getStatus()) + ")");
                closing = 2;
                setStateNotify(CLOSED);
            } else if (svc == serviceRequest) handleService(h, data, offset); else if (svc == serviceAck) {
                final ServiceAck res = new ServiceAck(svc, data, offset);
                if (res.getChannelID() != channelID) logger.warn("received wrong acknowledge channel-ID " + res.getChannelID() + ", expected " + channelID + " - ignored"); else if (res.getSequenceNumber() != getSeqNoSend()) logger.warn("received invalid acknowledge send-sequence " + +res.getSequenceNumber() + ", expected " + getSeqNoSend() + " - ignored"); else {
                    if (!checkVersion(h)) return;
                    incSeqNoSend();
                    setStateNotify(res.getStatus() == ErrorCodes.NO_ERROR ? CEMI_CON_PENDING : ACK_ERROR);
                    if (internalState == ACK_ERROR) logger.warn("received acknowledge status: " + res.getStatusString());
                }
            } else handleService(h, data, offset);
        }
