    public MetadataBlockDataStreamInfo(MetadataBlockHeader header, RandomAccessFile raf) throws IOException {
        ByteBuffer rawdata = ByteBuffer.allocate(header.getDataLength());
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < header.getDataLength()) {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
        }
        rawdata.rewind();
        minBlockSize = rawdata.getShort();
        maxBlockSize = rawdata.getShort();
        minFrameSize = readThreeByteInteger(rawdata.get(), rawdata.get(), rawdata.get());
        maxFrameSize = readThreeByteInteger(rawdata.get(), rawdata.get(), rawdata.get());
        samplingRate = readSamplingRate(rawdata.get(), rawdata.get(), rawdata.get());
        channelNumber = ((u(rawdata.get(12)) & 0x0E) >>> 1) + 1;
        samplingRatePerChannel = samplingRate / channelNumber;
        bitsPerSample = ((u(rawdata.get(12)) & 0x01) << 4) + ((u(rawdata.get(13)) & 0xF0) >>> 4) + 1;
        totalNumberOfSamples = readTotalNumberOfSamples(rawdata.get(13), rawdata.get(14), rawdata.get(15), rawdata.get(16), rawdata.get(17));
        length = (float) ((double) totalNumberOfSamples / samplingRatePerChannel);
        logger.info(this.toString());
    }
