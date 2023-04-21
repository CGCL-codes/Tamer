    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException {
        FlacStream.findStream(raf);
        MetadataBlockDataStreamInfo mbdsi = null;
        boolean isLastBlock = false;
        while (!isLastBlock) {
            MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(raf);
            if (mbh.getBlockType() == BlockType.STREAMINFO) {
                mbdsi = new MetadataBlockDataStreamInfo(mbh, raf);
                if (!mbdsi.isValid()) {
                    throw new CannotReadException("FLAC StreamInfo not valid");
                }
            } else {
                raf.seek(raf.getFilePointer() + mbh.getDataLength());
            }
            isLastBlock = mbh.isLastBlock();
            mbh = null;
        }
        if (mbdsi == null) {
            throw new CannotReadException("Unable to find Flac StreamInfo");
        }
        GenericAudioHeader info = new GenericAudioHeader();
        info.setLength(mbdsi.getLength());
        info.setPreciseLength(mbdsi.getPreciseLength());
        info.setChannelNumber(mbdsi.getChannelNumber());
        info.setSamplingRate(mbdsi.getSamplingRatePerChannel());
        info.setEncodingType(mbdsi.getEncodingType());
        info.setExtraEncodingInfos("");
        info.setBitrate(computeBitrate(mbdsi.getPreciseLength(), raf.length() - raf.getFilePointer()));
        return info;
    }
