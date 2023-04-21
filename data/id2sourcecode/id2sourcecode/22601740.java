    public boolean decode(ByteBuffer in, RtmpSession session) {
        final int position = in.position();
        header = new Header();
        if (!header.decode(in, session)) {
            return false;
        }
        final int channelId = header.getChannelId();
        Packet prevPacket = session.getPrevPacketsIn().get(channelId);
        int toReadRemaining = header.getSize();
        if (prevPacket != null) {
            toReadRemaining -= prevPacket.data.position();
        }
        final int chunkSize = session.getChunkSize();
        final int toReadNow = toReadRemaining > chunkSize ? chunkSize : toReadRemaining;
        if (in.remaining() < toReadNow) {
            return false;
        }
        session.getPrevHeadersIn().put(channelId, header);
        boolean isNewPacket = false;
        if (prevPacket == null) {
            isNewPacket = true;
            prevPacket = new Packet(header, header.getSize());
            session.getPrevPacketsIn().put(channelId, prevPacket);
        } else {
            header.setRelative(prevPacket.header.isRelative());
        }
        if (logger.isDebugEnabled()) {
            byte[] bytes = new byte[in.position() - position];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = in.get(position + i);
            }
            if (isNewPacket) {
                logger.debug("====================");
                logger.debug("starting new header: " + header + " <-- " + Utils.toHex(bytes));
            } else {
                logger.debug("resumed prev header: " + header + " <-- " + Utils.toHex(bytes) + "<-- " + prevPacket.header);
            }
        }
        data = prevPacket.data;
        byte[] bytes = new byte[toReadNow];
        in.get(bytes);
        data.put(bytes);
        if (data.position() == header.getSize()) {
            complete = true;
            session.getPrevPacketsIn().remove(channelId);
            data.flip();
        }
        return true;
    }
