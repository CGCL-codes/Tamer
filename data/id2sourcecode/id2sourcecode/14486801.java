    public EncodingInfo read(RandomAccessFile raf) throws CannotReadException, IOException {
        EncodingInfo encodingInfo = new EncodingInfo();
        if (raf.length() == 0) {
            System.err.println("Error: File empty");
            throw new CannotReadException("File is empty");
        }
        int startByte = 0;
        int id3TagSize = 0;
        raf.seek(0);
        byte[] bbb = new byte[3];
        raf.read(bbb);
        raf.seek(0);
        String ID3 = new String(bbb);
        if (ID3.equals("ID3")) {
            raf.seek(6);
            id3TagSize = read_syncsafe_integer(raf);
            raf.seek(id3TagSize + 10);
        }
        MPEGFrame firstFrame = null;
        byte[] b = new byte[4];
        raf.read(b);
        while (!((b[0] & 0xFF) == 0xFF && (b[1] & 0xE0) == 0xE0 && (b[1] & 0x06) != 0 && (b[2] & 0xF0) != 0xF0 && (b[2] & 0x0C) != 0x0C) && raf.getFilePointer() < raf.length() - 4) {
            raf.seek(raf.getFilePointer() - 3);
            raf.read(b);
        }
        firstFrame = new MPEGFrame(b);
        if (firstFrame == null || !firstFrame.isValid() || firstFrame.getSamplingRate() == 0) {
            throw new CannotReadException("Error: could not synchronize to first mp3 frame");
        }
        int firstFrameLength = firstFrame.getFrameLength();
        int skippedLength = 0;
        if (firstFrame.getMPEGVersion() == MPEGFrame.MPEG_VERSION_1 && firstFrame.getChannelMode() == MPEGFrame.CHANNEL_MODE_MONO) {
            raf.seek(raf.getFilePointer() + 17);
            skippedLength += 17;
        } else if (firstFrame.getMPEGVersion() == MPEGFrame.MPEG_VERSION_1) {
            raf.seek(raf.getFilePointer() + 32);
            skippedLength += 32;
        } else if (firstFrame.getMPEGVersion() == MPEGFrame.MPEG_VERSION_2 && firstFrame.getChannelMode() == MPEGFrame.CHANNEL_MODE_MONO) {
            raf.seek(raf.getFilePointer() + 9);
            skippedLength += 9;
        } else if (firstFrame.getMPEGVersion() == MPEGFrame.MPEG_VERSION_2) {
            raf.seek(raf.getFilePointer() + 17);
            skippedLength += 17;
        }
        int optionalFrameLength = 0;
        byte[] xingPart1 = new byte[16];
        raf.read(xingPart1);
        raf.seek(raf.getFilePointer() + 100);
        byte[] xingPart2 = new byte[4];
        raf.read(xingPart2);
        VbrInfoFrame vbrInfoFrame = new XingMPEGFrame(xingPart1, xingPart2);
        if (vbrInfoFrame.isValid()) {
            optionalFrameLength += 120;
            byte[] lameHeader = new byte[36];
            raf.read(lameHeader);
            LameMPEGFrame currentLameFrame = new LameMPEGFrame(lameHeader);
            if (!currentLameFrame.isValid()) raf.seek(raf.getFilePointer() - 36); else optionalFrameLength += 36;
            raf.seek(raf.getFilePointer() + firstFrameLength - (skippedLength + optionalFrameLength + 4));
        } else {
            raf.seek(raf.getFilePointer() - 120 - skippedLength + 32);
            byte[] vbriHeader = new byte[18];
            raf.read(vbriHeader);
            vbrInfoFrame = new VBRIMPEGFrame(vbriHeader);
            raf.seek(raf.getFilePointer() - 18 - 4);
        }
        double timePerFrame = ((double) firstFrame.getSampleNumber()) / firstFrame.getSamplingRate();
        double lengthInSeconds;
        if (vbrInfoFrame.isValid()) {
            lengthInSeconds = (timePerFrame * vbrInfoFrame.getFrameCount());
            encodingInfo.setVbr(vbrInfoFrame.isVbr());
            int fs = vbrInfoFrame.getFileSize();
            encodingInfo.setBitrate((int) (((fs == 0 ? raf.length() - id3TagSize : fs) * 8) / (timePerFrame * vbrInfoFrame.getFrameCount() * 1000)));
        } else {
            int frameLength = firstFrame.getFrameLength();
            if (frameLength == 0) throw new CannotReadException("Error while reading header(maybe file is corrupted, or missing first mpeg frame before xing header)");
            lengthInSeconds = timePerFrame * ((raf.length() - id3TagSize) / frameLength);
            encodingInfo.setVbr(false);
            encodingInfo.setBitrate(firstFrame.getBitrate());
        }
        encodingInfo.setPreciseLength((float) lengthInSeconds);
        encodingInfo.setChannelNumber(firstFrame.getChannelNumber());
        encodingInfo.setSamplingRate(firstFrame.getSamplingRate());
        encodingInfo.setEncodingType(firstFrame.MPEGVersionToString(firstFrame.getMPEGVersion()) + " || " + firstFrame.layerToString(firstFrame.getLayerVersion()));
        encodingInfo.setExtraEncodingInfos("");
        return encodingInfo;
    }
