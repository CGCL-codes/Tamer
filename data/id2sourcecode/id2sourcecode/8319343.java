    public void setChannelPosition(Block b, BlockWriteStreams streams, long dataOffset, long ckOffset) throws IOException {
        long size = 0;
        synchronized (this) {
            FSVolume vol = volumeMap.get(b).getVolume();
            size = vol.getTmpFile(b).length();
        }
        if (size < dataOffset) {
            String msg = "Trying to change block file offset of block " + b + " to " + dataOffset + " but actual size of file is " + size;
            throw new IOException(msg);
        }
        FileOutputStream file = (FileOutputStream) streams.dataOut;
        file.getChannel().position(dataOffset);
        file = (FileOutputStream) streams.checksumOut;
        file.getChannel().position(ckOffset);
    }
