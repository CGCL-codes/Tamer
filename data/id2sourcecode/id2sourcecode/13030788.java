    void handleService(KNXnetIPHeader h, byte[] data, int offset) throws KNXFormatException, IOException {
        final int svc = h.getServiceType();
        if (svc == KNXnetIPHeader.DEVICE_CONFIGURATION_REQ) {
            ServiceRequest req;
            try {
                req = PacketHelper.getServiceRequest(h, data, offset);
            } catch (final KNXFormatException e) {
                req = PacketHelper.getEmptyServiceRequest(h, data, offset);
                final byte[] junk = new byte[h.getTotalLength() - h.getStructLength() - 4];
                System.arraycopy(data, offset + 4, junk, 0, junk.length);
                logger.warn("received dev.mgmt request with unknown cEMI part " + DataUnitBuilder.toHex(junk, " "), e);
            }
            final short seq = req.getSequenceNumber();
            if (req.getChannelID() == getChannelID() && seq == getSeqNoRcv()) {
                final short status = h.getVersion() == KNXNETIP_VERSION_10 ? ErrorCodes.NO_ERROR : ErrorCodes.VERSION_NOT_SUPPORTED;
                final byte[] buf = PacketHelper.toPacket(new ServiceAck(KNXnetIPHeader.DEVICE_CONFIGURATION_ACK, getChannelID(), seq, status));
                final DatagramPacket p = new DatagramPacket(buf, buf.length, dataEP.getAddress(), dataEP.getPort());
                socket.send(p);
                incSeqNoRcv();
                if (status == ErrorCodes.VERSION_NOT_SUPPORTED) {
                    close(ConnectionCloseEvent.INTERNAL, "protocol version changed", LogLevel.ERROR, null);
                    return;
                }
                final CEMI cemi = req.getCEMI();
                if (cemi == null) return;
                final short mc = cemi.getMessageCode();
                if (mc == CEMIDevMgmt.MC_PROPINFO_IND || mc == CEMIDevMgmt.MC_RESET_IND) fireFrameReceived(cemi); else if (mc == CEMIDevMgmt.MC_PROPREAD_CON || mc == CEMIDevMgmt.MC_PROPWRITE_CON) {
                    fireFrameReceived(cemi);
                    setStateNotify(OK);
                }
            } else logger.warn("received dev.mgmt request channel-ID " + req.getChannelID() + ", receive-sequence " + seq + ", expected " + getSeqNoRcv() + " - ignored");
        } else logger.warn("received unknown frame (service type 0x" + Integer.toHexString(svc) + ") - ignored");
    }
