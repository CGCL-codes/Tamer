    public static void readSnapshot(ReadableByteChannel channel, DirectoryManager dirMgr) throws IOException {
        int formatVersion = ChannelUtil.readInt(channel);
        if (formatVersion != 1) {
            throw new IOException("snapshot format version mismatch [" + formatVersion + "]");
        }
        if (!dirMgr.transferFromChannelToFile(channel, DirectoryManager.INDEX_DIRECTORY)) {
            throw new IOException("bad snapshot file");
        }
        int numFiles = ChannelUtil.readInt(channel);
        if (numFiles < 0) {
            throw new IOException("bad snapshot file");
        }
        while (numFiles-- > 0) {
            String fileName = ChannelUtil.readString(channel);
            if (fileName == null) {
                throw new IOException("bad snapshot file");
            }
            if (!dirMgr.transferFromChannelToFile(channel, fileName)) {
                throw new IOException("bad snapshot file");
            }
        }
    }
