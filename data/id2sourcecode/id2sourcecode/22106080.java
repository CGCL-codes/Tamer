    private void replacePagesAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes, int newCommentLength, OggPageHeader secondPageHeader, ByteBuffer newComment, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        int pageSequence = secondPageHeader.getPageSequence();
        int noOfPagesNeededForComment = newCommentLength / OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.info("Comment requires:" + noOfPagesNeededForComment + " complete pages");
        int newCommentOffset = 0;
        for (int i = 0; i < noOfPagesNeededForComment; i++) {
            byte[] segmentTable = this.createSegments(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE, false);
            int pageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer pageBuffer = ByteBuffer.allocate(pageHeaderLength + OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
            pageBuffer.order(ByteOrder.LITTLE_ENDIAN);
            pageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            pageBuffer.put((byte) segmentTable.length);
            for (int j = 0; j < segmentTable.length; j++) {
                pageBuffer.put(segmentTable[j]);
            }
            pageBuffer.put(newComment.array(), newCommentOffset, OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
            pageBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            pageSequence++;
            if (i != 0) {
                pageBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            }
            pageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(pageBuffer.array());
            for (int j = 0; j < crc.length; j++) {
                pageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + j, crc[j]);
            }
            pageBuffer.rewind();
            rafTemp.getChannel().write(pageBuffer);
            newCommentOffset += OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        }
        int lastPageCommentPacketSize = newCommentLength % OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.fine("Last comment packet size:" + lastPageCommentPacketSize);
        if (!isCommentAndSetupHeaderFitsOnASinglePage(lastPageCommentPacketSize, originalHeaderSizes.getSetupHeaderSize())) {
            logger.fine("Spread over two pages");
            byte[] commentSegmentTable = createSegments(lastPageCommentPacketSize, true);
            int remainingSegmentSlots = OggPageHeader.MAXIMUM_NO_OF_SEGMENT_SIZE - commentSegmentTable.length;
            int firstHalfOfHeaderSize = remainingSegmentSlots * OggPageHeader.MAXIMUM_SEGMENT_SIZE;
            byte[] firstHalfofSegmentHeaderTable = createSegments(firstHalfOfHeaderSize, false);
            byte[] segmentTable = createSegmentTable(lastPageCommentPacketSize, firstHalfOfHeaderSize);
            int lastCommentHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + commentSegmentTable.length + firstHalfofSegmentHeaderTable.length;
            ByteBuffer lastCommentHeaderBuffer = ByteBuffer.allocate(lastCommentHeaderLength + lastPageCommentPacketSize + firstHalfOfHeaderSize);
            lastCommentHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastCommentHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lastCommentHeaderBuffer.put((byte) segmentTable.length);
            for (int i = 0; i < segmentTable.length; i++) {
                lastCommentHeaderBuffer.put(segmentTable[i]);
            }
            lastCommentHeaderBuffer.put(newComment.array(), newCommentOffset, lastPageCommentPacketSize);
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
            logger.finest(setupHeaderData.length + ":" + lastCommentHeaderBuffer.position() + ":" + lastCommentHeaderBuffer.capacity());
            int copyAmount = setupHeaderData.length;
            if (setupHeaderData.length > lastCommentHeaderBuffer.remaining()) {
                copyAmount = lastCommentHeaderBuffer.remaining();
                logger.finest("Copying :" + copyAmount);
            }
            lastCommentHeaderBuffer.put(setupHeaderData, 0, copyAmount);
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastCommentHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            pageSequence++;
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(lastCommentHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                lastCommentHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            lastCommentHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastCommentHeaderBuffer);
            int secondHalfOfHeaderSize = originalHeaderSizes.getSetupHeaderSize() - firstHalfOfHeaderSize;
            segmentTable = createSegmentTable(secondHalfOfHeaderSize, 0);
            int lastSetupHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer lastSetupHeaderBuffer = ByteBuffer.allocate(lastSetupHeaderLength + secondHalfOfHeaderSize);
            lastSetupHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastSetupHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lastSetupHeaderBuffer.put((byte) segmentTable.length);
            for (int i = 0; i < segmentTable.length; i++) {
                lastSetupHeaderBuffer.put(segmentTable[i]);
            }
            logger.finest(setupHeaderData.length - copyAmount + ":" + lastSetupHeaderBuffer.position() + ":" + lastSetupHeaderBuffer.capacity());
            lastSetupHeaderBuffer.put(setupHeaderData, copyAmount, setupHeaderData.length - copyAmount);
            lastSetupHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastSetupHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            lastSetupHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            crc = OggCRCFactory.computeCRC(lastSetupHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                lastSetupHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            lastSetupHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastSetupHeaderBuffer);
        } else {
            logger.fine("Setupheader fits on comment page");
            byte[] segmentTable = createSegmentTable(lastPageCommentPacketSize, originalHeaderSizes.getSetupHeaderSize());
            int lastHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer lastCommentHeaderBuffer = ByteBuffer.allocate(lastHeaderLength + lastPageCommentPacketSize + originalHeaderSizes.getSetupHeaderSize());
            lastCommentHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastCommentHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lastCommentHeaderBuffer.put((byte) segmentTable.length);
            for (int i = 0; i < segmentTable.length; i++) {
                lastCommentHeaderBuffer.put(segmentTable[i]);
            }
            lastCommentHeaderBuffer.put(newComment.array(), newCommentOffset, lastPageCommentPacketSize);
            raf.seek(originalHeaderSizes.getSetupHeaderStartPosition());
            OggPageHeader setupPageHeader;
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
            lastCommentHeaderBuffer.put(setupHeaderData);
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastCommentHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(lastCommentHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                lastCommentHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            lastCommentHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastCommentHeaderBuffer);
        }
        long startAudio = raf.getFilePointer();
        long startAudioWritten = rafTemp.getFilePointer();
        logger.fine("Writing audio, audio starts in original file at :" + startAudio + ":Written to:" + startAudioWritten);
        while (raf.getFilePointer() < raf.length()) {
            logger.fine("Reading Ogg Page");
            OggPageHeader nextPage = OggPageHeader.read(raf);
            ByteBuffer nextPageHeaderBuffer = ByteBuffer.allocate(nextPage.getRawHeaderData().length + nextPage.getPageLength());
            nextPageHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            nextPageHeaderBuffer.put(nextPage.getRawHeaderData());
            raf.getChannel().read(nextPageHeaderBuffer);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, ++pageSequence);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(nextPageHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                nextPageHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            nextPageHeaderBuffer.rewind();
            rafTemp.getChannel().write(nextPageHeaderBuffer);
        }
        if ((raf.length() - startAudio) != (rafTemp.length() - startAudioWritten)) {
            throw new CannotWriteException("File written counts dont macth, file not written");
        }
    }
