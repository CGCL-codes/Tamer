    private void writeTrack(TGTrack track) throws IOException {
        GMChannelRoute channel = getChannelRoute(track.getChannelId());
        int flags = 0;
        if (isPercussionChannel(track.getSong(), track.getChannelId())) {
            flags |= 0x01;
        }
        writeUnsignedByte(flags);
        writeUnsignedByte((8 | flags));
        writeStringByte(track.getName(), 40);
        writeInt(track.getStrings().size());
        for (int i = 0; i < 7; i++) {
            int value = 0;
            if (track.getStrings().size() > i) {
                TGString string = (TGString) track.getStrings().get(i);
                value = string.getValue();
            }
            writeInt(value);
        }
        writeInt(1);
        writeInt(channel.getChannel1() + 1);
        writeInt(channel.getChannel2() + 1);
        writeInt(24);
        writeInt(track.getOffset());
        writeColor(track.getColor());
        writeBytes(new byte[] { 67, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -1, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 });
    }
