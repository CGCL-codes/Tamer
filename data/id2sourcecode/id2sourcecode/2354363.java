    protected SendRvIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_SEND_ICBM, packet);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock channelData = getChannelData();
        StringBlock snInfo = OscarTools.readScreenname(channelData);
        sn = snInfo.getString();
        ByteBlock tlvBlock = channelData.subBlock(snInfo.getTotalSize());
        TlvChain chain = TlvTools.readChain(tlvBlock);
        processRvTlvs(chain);
    }
