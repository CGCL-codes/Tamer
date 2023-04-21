    public void handle(Packet p) {
        Packet response = new Packet(PacketTypeEnum.APPLICATION, new Long(getNextPrime()));
        response.setChannelID(p.getChannelID());
        try {
            networkPort.send(getContext().createMessage(response));
        } catch (PortException e) {
            LOG.error("Unable to send response packet: ", e);
        }
    }
