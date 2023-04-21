    public OldIcbm(SnacPacket packet) {
        super(IcbmCommand.CMD_ICBM, packet);
        requestID = packet.getReqid();
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = getChannelData();
        userInfo = FullUserInfo.readUserInfo(snacData);
        ByteBlock tlvBlock = snacData.subBlock(userInfo.getTotalSize());
        TlvChain chain = TlvTools.readChain(tlvBlock);
        Tlv messageDataTlv = chain.getLastTlv(TYPE_MESSAGE_DATA);
        ByteBlock messageData = messageDataTlv.getData();
        sender = LEBinaryTools.getUInt(messageData, 0);
        messageType = LEBinaryTools.getUByte(messageData, 4);
        short msgFlags = LEBinaryTools.getUByte(messageData, 5);
        int textlen = LEBinaryTools.getUShort(messageData, 6) - 1;
        ByteBlock field = messageData.subBlock(8, textlen);
        ImEncodingParams encoding = new ImEncodingParams(0);
        reason = ImEncodedString.readImEncodedString(encoding, field);
    }
