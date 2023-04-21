    SSH2TransportPDU getChannelOpenPDU(SSH2Channel channel) {
        SSH2TransportPDU pdu = SSH2TransportPDU.createOutgoingPacket(SSH2.MSG_CHANNEL_OPEN);
        pdu.writeString(channelTypes[channel.channelType]);
        pdu.writeInt(channel.channelId);
        pdu.writeInt(channel.rxInitWinSz);
        pdu.writeInt(channel.rxMaxPktSz);
        return pdu;
    }
