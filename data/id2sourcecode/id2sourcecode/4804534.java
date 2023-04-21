    public void processUpdate(ChannelList channelList) throws ProtocolException {
        updateBuffer.order(ByteOrder.LITTLE_ENDIAN);
        updateBuffer.rewind();
        byte[] raw = updateBuffer.array();
        short plannedChannels = updateBuffer.getShort(0x00);
        int currentChannel = 1;
        int chanOffset = 4;
        int seqPos = 0;
        while (currentChannel <= plannedChannels) {
            int channelID = updateBuffer.getInt(chanOffset + 0x00);
            byte flags = updateBuffer.get(chanOffset + 0x04);
            byte codec = updateBuffer.get(chanOffset + 0x06);
            int parentChannelId = updateBuffer.getInt(chanOffset + 0x08);
            short order = updateBuffer.getShort(chanOffset + 0x0c);
            short maxUsers = updateBuffer.getShort(chanOffset + 0x0e);
            StringBuffer name = new StringBuffer();
            StringBuffer topic = new StringBuffer();
            StringBuffer description = new StringBuffer();
            seqPos = chanOffset + 0x10;
            while (raw[seqPos] != 0x00 && seqPos < raw.length) {
                name.append((char) raw[seqPos]);
                seqPos++;
            }
            seqPos++;
            while (raw[seqPos] != 0x00 && seqPos < raw.length) {
                topic.append((char) raw[seqPos]);
                seqPos++;
            }
            seqPos++;
            while (raw[seqPos] != 0x00 && seqPos < raw.length) {
                description.append((char) raw[seqPos]);
                seqPos++;
            }
            seqPos++;
            Channel c;
            Channel alreadyExists = channelList.getChannelById(channelID);
            if (alreadyExists != null) {
                c = alreadyExists;
            } else {
                c = new Channel(channelID, name.toString());
            }
            c.setTopic(topic.toString());
            c.setDescription(description.toString());
            c.setOrder(order);
            c.setMaxUsers(maxUsers);
            c.getFlags().clear();
            c.getFlags().addAll(ChannelAttributeSet.fromByte(flags));
            c.setCodec(Codec.fromByte(codec));
            if (alreadyExists == null) {
                if (parentChannelId == ChannelList.CHANNEL_AT_ROOTLEVEL) {
                    channelList.addToplevelChannel(c);
                } else {
                    Channel parent = channelList.getChannelById(parentChannelId);
                    if (parent == null) throw new ProtocolException("Recived ChannelUpdate without knowing ParentChannel.");
                    parent.addChannel(c);
                }
            }
            currentChannel++;
            chanOffset = seqPos;
        }
    }
