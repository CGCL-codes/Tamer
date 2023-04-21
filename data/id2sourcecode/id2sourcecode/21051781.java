    public EncodingInfo read(RandomAccessFile raf) throws CannotReadException, IOException {
        if (raf.length() == 0) {
            throw new CannotReadException("Error: File empty");
        }
        raf.seek(0);
        byte[] b = new byte[4];
        raf.read(b);
        String flac = new String(b);
        if (!flac.equals("fLaC")) {
            throw new CannotReadException("fLaC Header not found");
        }
        MetadataBlockDataStreamInfo mbdsi = null;
        boolean isLastBlock = false;
        while (!isLastBlock) {
            b = new byte[4];
            raf.read(b);
            MetadataBlockHeader mbh = new MetadataBlockHeader(b);
            if (mbh.getBlockType() == MetadataBlockHeader.STREAMINFO) {
                b = new byte[mbh.getDataLength()];
                raf.read(b);
                mbdsi = new MetadataBlockDataStreamInfo(b);
                if (!mbdsi.isValid()) {
                    throw new CannotReadException("FLAC StreamInfo not valid");
                }
                break;
            }
            raf.seek(raf.getFilePointer() + mbh.getDataLength());
            isLastBlock = mbh.isLastBlock();
            mbh = null;
        }
        assert mbdsi != null;
        EncodingInfo info = new EncodingInfo();
        info.setPreciseLength(mbdsi.getPreciseLength());
        info.setChannelNumber(mbdsi.getChannelNumber());
        info.setSamplingRate(mbdsi.getSamplingRate());
        info.setEncodingType(mbdsi.getEncodingType());
        info.setExtraEncodingInfos("");
        info.setBitrate(computeBitrate(mbdsi.getLength(), raf.length()));
        return info;
    }
