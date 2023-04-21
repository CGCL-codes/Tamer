    public EncodingInfo read(RandomAccessFile raf) throws CannotReadException, IOException {
        EncodingInfo info = new EncodingInfo();
        long oldPos = 0;
        raf.seek(0);
        double PCMSamplesNumber = -1;
        raf.seek(raf.length() - 2);
        while (raf.getFilePointer() >= 4) {
            if (raf.read() == 0x53) {
                raf.seek(raf.getFilePointer() - 4);
                byte[] ogg = new byte[3];
                raf.readFully(ogg);
                if (ogg[0] == 0x4F && ogg[1] == 0x67 && ogg[2] == 0x67) {
                    raf.seek(raf.getFilePointer() - 3);
                    oldPos = raf.getFilePointer();
                    raf.seek(raf.getFilePointer() + 26);
                    int pageSegments = raf.readByte() & 0xFF;
                    raf.seek(oldPos);
                    byte[] b = new byte[27 + pageSegments];
                    raf.readFully(b);
                    OggPageHeader pageHeader = new OggPageHeader(b);
                    raf.seek(0);
                    PCMSamplesNumber = pageHeader.getAbsoluteGranulePosition();
                    break;
                }
            }
            raf.seek(raf.getFilePointer() - 2);
        }
        if (PCMSamplesNumber == -1) {
            throw new CannotReadException("Error: Could not find the Ogg Setup block");
        }
        byte[] b = new byte[4];
        oldPos = raf.getFilePointer();
        raf.seek(26);
        int pageSegments = raf.read() & 0xFF;
        raf.seek(oldPos);
        b = new byte[27 + pageSegments];
        raf.read(b);
        OggPageHeader pageHeader = new OggPageHeader(b);
        byte[] vorbisData = new byte[pageHeader.getPageLength()];
        raf.read(vorbisData);
        VorbisCodecHeader vorbisCodecHeader = new VorbisCodecHeader(vorbisData);
        info.setPreciseLength((float) (PCMSamplesNumber / vorbisCodecHeader.getSamplingRate()));
        info.setChannelNumber(vorbisCodecHeader.getChannelNumber());
        info.setSamplingRate(vorbisCodecHeader.getSamplingRate());
        info.setEncodingType(vorbisCodecHeader.getEncodingType());
        info.setExtraEncodingInfos("");
        if (vorbisCodecHeader.getNominalBitrate() != 0 && vorbisCodecHeader.getMaxBitrate() == vorbisCodecHeader.getNominalBitrate() && vorbisCodecHeader.getMinBitrate() == vorbisCodecHeader.getNominalBitrate()) {
            info.setBitrate(vorbisCodecHeader.getNominalBitrate() / 1000);
            info.setVbr(false);
        } else if (vorbisCodecHeader.getNominalBitrate() != 0 && vorbisCodecHeader.getMaxBitrate() == 0 && vorbisCodecHeader.getMinBitrate() == 0) {
            info.setBitrate(vorbisCodecHeader.getNominalBitrate() / 1000);
            info.setVbr(true);
        } else {
            info.setBitrate(computeBitrate(info.getLength(), raf.length()));
            info.setVbr(true);
        }
        return info;
    }
