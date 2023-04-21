    private CZLSMInfo getCZ_LSMINFO(RandomAccessStream stream, long position, boolean thumb) {
        CZLSMInfo cz = new CZLSMInfo();
        try {
            if (position == 0) return cz;
            stream.seek((int) position + 8);
            cz.DimensionX = swap(stream.readInt());
            cz.DimensionY = swap(stream.readInt());
            cz.DimensionZ = swap(stream.readInt());
            cz.DimensionChannels = swap(stream.readInt());
            cz.DimensionTime = swap(stream.readInt());
            cz.IntensityDataType = swap(stream.readInt());
            cz.ThumbnailX = swap(stream.readInt());
            cz.ThumbnailY = swap(stream.readInt());
            cz.VoxelSizeX = swap(stream.readDouble());
            cz.VoxelSizeY = swap(stream.readDouble());
            cz.VoxelSizeZ = swap(stream.readDouble());
            stream.seek((int) position + 88);
            cz.ScanType = swap(stream.readShort());
            stream.seek((int) position + 108);
            cz.OffsetChannelColors = swap(stream.readInt());
            stream.seek((int) position + 120);
            cz.OffsetChannelDataTypes = swap(stream.readInt());
            if (cz.OffsetChannelDataTypes != 0) {
                cz.OffsetChannelDataTypesValues = getOffsetChannelDataTypesValues(stream, cz.OffsetChannelDataTypes, cz.DimensionChannels);
            }
            if (cz.OffsetChannelColors != 0) {
                ChannelNamesAndColors channelNamesAndColors = getChannelNamesAndColors(stream, cz.OffsetChannelColors, cz.DimensionChannels);
                cz.channelNamesAndColors = channelNamesAndColors;
            }
        } catch (IOException getCZ_LSMINFO_exception) {
            getCZ_LSMINFO_exception.printStackTrace();
        }
        return cz;
    }
