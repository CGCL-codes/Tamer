    protected SendImIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_SEND_ICBM, packet);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = getChannelData();
        StringBlock snInfo = OscarTools.readScreenname(snacData);
        sn = snInfo.getString();
        ByteBlock rest = snacData.subBlock(snInfo.getTotalSize());
        TlvChain imTlvs = TlvTools.readChain(rest);
        processImTlvs(imTlvs);
        ackRequested = imTlvs.hasTlv(TYPE_ACK);
    }
