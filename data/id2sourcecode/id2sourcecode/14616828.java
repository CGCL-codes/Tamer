    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException {
        Mp4AudioHeader info = new Mp4AudioHeader();
        Mp4BoxHeader ftypHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.FTYP.getFieldName());
        if (ftypHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_CONTAINER.getMsg());
        }
        ByteBuffer ftypBuffer = ByteBuffer.allocate(ftypHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(ftypBuffer);
        ftypBuffer.rewind();
        Mp4FtypBox ftyp = new Mp4FtypBox(ftypHeader, ftypBuffer);
        ftyp.processData();
        info.setBrand(ftyp.getMajorBrand());
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.MOOV.getFieldName());
        if (moovHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.MVHD.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        ByteBuffer mvhdBuffer = moovBuffer.slice();
        Mp4MvhdBox mvhd = new Mp4MvhdBox(boxHeader, mvhdBuffer);
        info.setLength(mvhd.getLength());
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.TRAK.getFieldName());
        int endOfFirstTrackInBuffer = mvhdBuffer.position() + boxHeader.getDataLength();
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MDIA.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MDHD.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        Mp4MdhdBox mdhd = new Mp4MdhdBox(boxHeader, mvhdBuffer.slice());
        info.setSamplingRate(mdhd.getSampleRate());
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MINF.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.SMHD.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.STBL.getFieldName());
        if (boxHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO.getMsg());
        }
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.STSD.getFieldName());
        if (boxHeader != null) {
            Mp4StsdBox stsd = new Mp4StsdBox(boxHeader, mvhdBuffer);
            stsd.processData();
            int positionAfterStsdHeaderAndData = mvhdBuffer.position();
            boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MP4A.getFieldName());
            if (boxHeader != null) {
                ByteBuffer mp4aBuffer = mvhdBuffer.slice();
                Mp4Mp4aBox mp4a = new Mp4Mp4aBox(boxHeader, mp4aBuffer);
                mp4a.processData();
                boxHeader = Mp4BoxHeader.seekWithinLevel(mp4aBuffer, Mp4NotMetaFieldKey.ESDS.getFieldName());
                if (boxHeader != null) {
                    Mp4EsdsBox esds = new Mp4EsdsBox(boxHeader, mp4aBuffer.slice());
                    info.setBitrate(esds.getAvgBitrate() / 1000);
                    info.setChannelNumber(esds.getNumberOfChannels());
                    info.setKind(esds.getKind());
                    info.setProfile(esds.getAudioProfile());
                    info.setEncodingType(EncoderType.AAC.getDescription());
                }
            } else {
                mvhdBuffer.position(positionAfterStsdHeaderAndData);
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.DRMS.getFieldName());
                if (boxHeader != null) {
                    Mp4DrmsBox drms = new Mp4DrmsBox(boxHeader, mvhdBuffer);
                    drms.processData();
                    boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.ESDS.getFieldName());
                    if (boxHeader != null) {
                        Mp4EsdsBox esds = new Mp4EsdsBox(boxHeader, mvhdBuffer.slice());
                        info.setBitrate(esds.getAvgBitrate() / 1000);
                        info.setChannelNumber(esds.getNumberOfChannels());
                        info.setKind(esds.getKind());
                        info.setProfile(esds.getAudioProfile());
                        info.setEncodingType(EncoderType.DRM_AAC.getDescription());
                    }
                } else {
                    mvhdBuffer.position(positionAfterStsdHeaderAndData);
                    boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.ALAC.getFieldName());
                    if (boxHeader != null) {
                        Mp4AlacBox alac = new Mp4AlacBox(boxHeader, mvhdBuffer);
                        alac.processData();
                        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.ALAC.getFieldName());
                        if (boxHeader != null) {
                            alac = new Mp4AlacBox(boxHeader, mvhdBuffer);
                            alac.processData();
                            info.setEncodingType(EncoderType.APPLE_LOSSLESS.getDescription());
                            info.setChannelNumber(alac.getChannels());
                            info.setBitrate(alac.getBitRate() / 1000);
                        }
                    }
                }
            }
        }
        if (info.getChannelNumber() == -1) {
            info.setChannelNumber(2);
        }
        if (info.getBitRateAsNumber() == -1) {
            info.setBitrate(128);
        }
        if (info.getEncodingType().equals("")) {
            info.setEncodingType(EncoderType.AAC.getDescription());
        }
        logger.info(info.toString());
        mvhdBuffer.position(endOfFirstTrackInBuffer);
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.TRAK.getFieldName());
        if (boxHeader != null) {
            if (ftyp.getMajorBrand().equals(Mp4FtypBox.Brand.ISO14496_1_VERSION_2.getId()) || ftyp.getMajorBrand().equals(Mp4FtypBox.Brand.APPLE_AUDIO_ONLY.getId()) || ftyp.getMajorBrand().equals(Mp4FtypBox.Brand.APPLE_AUDIO.getId())) {
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MDIA.getFieldName());
                if (boxHeader == null) {
                    throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
                }
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MDHD.getFieldName());
                if (boxHeader == null) {
                    throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
                }
                mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.MINF.getFieldName());
                if (boxHeader == null) {
                    throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
                }
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer, Mp4NotMetaFieldKey.NMHD.getFieldName());
                if (boxHeader == null) {
                    throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
                }
            } else {
                logger.info(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg() + ":" + ftyp.getMajorBrand());
                throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO.getMsg());
            }
        }
        Mp4AtomTree atomTree = new Mp4AtomTree(raf, false);
        return info;
    }
