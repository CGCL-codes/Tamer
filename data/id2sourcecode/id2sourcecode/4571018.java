    private ChannelNamesAndColors getChannelNamesAndColors(RandomAccessStream stream, long position, long channelCount) {
        ChannelNamesAndColors channelNamesAndColors = new ChannelNamesAndColors();
        try {
            stream.seek((int) position);
            channelNamesAndColors.BlockSize = swap(stream.readInt());
            channelNamesAndColors.NumberColors = swap(stream.readInt());
            channelNamesAndColors.NumberNames = swap(stream.readInt());
            channelNamesAndColors.ColorsOffset = swap(stream.readInt());
            channelNamesAndColors.NamesOffset = swap(stream.readInt());
            channelNamesAndColors.Mono = swap(stream.readInt());
            stream.seek((int) channelNamesAndColors.NamesOffset + (int) position);
            channelNamesAndColors.ChannelNames = new String[(int) channelCount];
            for (int j = 0; j < channelCount; j++) {
                long size = swap(stream.readInt());
                channelNamesAndColors.ChannelNames[j] = readSizedNULLASCII(stream, size);
            }
            stream.seek((int) channelNamesAndColors.ColorsOffset + (int) position);
            channelNamesAndColors.Colors = new int[(int) (channelNamesAndColors.NumberColors)];
            for (int j = 0; j < (int) (channelNamesAndColors.NumberColors); j++) {
                channelNamesAndColors.Colors[j] = swap(stream.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channelNamesAndColors;
    }
