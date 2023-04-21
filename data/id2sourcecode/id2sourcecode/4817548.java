    private void disconnectRequested(DisconnectRequest req) {
        if (req.getChannelID() == channelID) {
            final byte[] buf = PacketHelper.toPacket(new DisconnectResponse(channelID, ErrorCodes.NO_ERROR));
            final DatagramPacket p = new DatagramPacket(buf, buf.length, ctrlEP.getAddress(), ctrlEP.getPort());
            try {
                socket.send(p);
            } catch (final IOException e) {
                logger.error("communication failure", e);
            } finally {
                shutdown(SERVER, "requested by server", LogLevel.INFO, null);
            }
        }
    }
