    protected RecvRvIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_ICBM, packet);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock channelData = getChannelData();
        sender = FullUserInfo.readUserInfo(channelData);
        ByteBlock tlvBlock = channelData.subBlock(sender.getTotalSize());
        TlvChain chain = TlvTools.readChain(tlvBlock);
        processRvTlvs(chain);
    }
