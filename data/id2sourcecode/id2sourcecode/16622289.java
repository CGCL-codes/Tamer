    protected RecvImIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_ICBM, packet);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = getChannelData();
        userInfo = FullUserInfo.readUserInfo(snacData);
        ByteBlock tlvBlock = snacData.subBlock(userInfo.getTotalSize());
        TlvChain chain = TlvTools.readChain(tlvBlock);
        processImTlvs(chain);
        canType = chain.hasTlv(TYPE_CAN_TYPE);
    }
