        void sendChannelMessage(String channelName, int seq) throws Exception {
            checkLoggedIn();
            BigInteger channelRefId = channelNameToId.get(channelName);
            if (channelRefId == null) {
                channelRefId = getChannelId(channelName);
            }
            System.err.println(toString() + " sending message:" + seq + " to channel:" + channelName);
            byte[] channelId = channelRefId.toByteArray();
            MessageBuffer buf = new MessageBuffer(3 + channelId.length + 4);
            buf.putByte(SimpleSgsProtocol.CHANNEL_MESSAGE).putShort(channelId.length).putBytes(channelId).putInt(seq);
            sendRaw(buf.getBuffer(), true);
        }
