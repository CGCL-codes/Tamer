    public Mp4Tag read(RandomAccessFile raf) throws CannotReadException, IOException {
        Mp4Tag tag = new Mp4Tag();
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.MOOV.getFieldName());
        if (moovHeader == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_CONTAINER.getMsg());
        }
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.UDTA.getFieldName());
        if (boxHeader != null) {
            boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
            if (boxHeader == null) {
                logger.warning(ErrorMessage.MP4_FILE_HAS_NO_METADATA.getMsg());
                return tag;
            }
            Mp4MetaBox meta = new Mp4MetaBox(boxHeader, moovBuffer);
            meta.processData();
            boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.ILST.getFieldName());
            if (boxHeader == null) {
                logger.warning(ErrorMessage.MP4_FILE_HAS_NO_METADATA.getMsg());
                return tag;
            }
        } else {
            boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
            if (boxHeader == null) {
                logger.warning(ErrorMessage.MP4_FILE_HAS_NO_METADATA.getMsg());
                return tag;
            }
            Mp4MetaBox meta = new Mp4MetaBox(boxHeader, moovBuffer);
            meta.processData();
            boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.ILST.getFieldName());
            if (boxHeader == null) {
                logger.warning(ErrorMessage.MP4_FILE_HAS_NO_METADATA.getMsg());
                return tag;
            }
        }
        int length = boxHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH;
        ByteBuffer metadataBuffer = moovBuffer.slice();
        logger.info("headerlengthsays:" + length + "datalength:" + metadataBuffer.limit());
        int read = 0;
        logger.info("Started to read metadata fields at position is in metadata buffer:" + metadataBuffer.position());
        while (read < length) {
            boxHeader.update(metadataBuffer);
            logger.info("Next position is at:" + metadataBuffer.position());
            createMp4Field(tag, boxHeader, metadataBuffer.slice());
            metadataBuffer.position(metadataBuffer.position() + boxHeader.getDataLength());
            read += boxHeader.getLength();
        }
        return tag;
    }
