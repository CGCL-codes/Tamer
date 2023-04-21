    protected AbstractChatMsgIcbm(int command, SnacPacket packet) {
        super(ChatCommand.FAMILY_CHAT, command, packet);
        TlvChain chain = TlvTools.readChain(getChannelData());
        Tlv msgTlv = chain.getLastTlv(TYPE_MSGBLOCK);
        if (msgTlv != null) {
            ByteBlock msgBlock = msgTlv.getData();
            chatMsg = ChatMsg.readChatMsg(msgBlock);
        } else {
            chatMsg = null;
        }
        chatTlvs = chain;
    }
