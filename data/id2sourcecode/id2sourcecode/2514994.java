    FlapPacket(FlapHeader header, ByteBlock data) throws IllegalArgumentException {
        DefensiveTools.checkNull(header, "header");
        DefensiveTools.checkNull(data, "data");
        if (header.getDataLength() != data.getLength()) {
            throw new IllegalArgumentException("FLAP data length (" + data.getLength() + ") does not agree with header (" + header.getDataLength() + ")");
        }
        channel = header.getChannel();
        seqnum = header.getSeqnum();
        this.block = data;
        this.command = null;
    }
