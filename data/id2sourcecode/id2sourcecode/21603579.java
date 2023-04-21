    public MetadataBlockDataPicture(MetadataBlockHeader header, RandomAccessFile raf) throws IOException, InvalidFrameException {
        ByteBuffer rawdata = ByteBuffer.allocate(header.getDataLength());
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < header.getDataLength()) {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
        }
        rawdata.rewind();
        initFromByteBuffer(rawdata);
    }
