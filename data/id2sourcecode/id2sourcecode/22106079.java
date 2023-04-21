    private void replaceSecondPageAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        byte[] segmentTable = createSegmentTable(newCommentLength, originalHeaderSizes.getSetupHeaderSize());
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
        secondPageBuffer.order(ByteOrder.LITTLE_ENDIAN);
        secondPageBuffer.put((byte) segmentTable.length);
        for (int i = 0; i < segmentTable.length; i++) {
            secondPageBuffer.put(segmentTable[i]);
        }
        secondPageBuffer.put(newComment);
        int pageSequence = secondPageHeader.getPageSequence();
        byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
        logger.finest(setupHeaderData.length + ":" + secondPageBuffer.position() + ":" + secondPageBuffer.capacity());
        secondPageBuffer.put(setupHeaderData);
        secondPageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++) {
            secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);
        long startAudio = raf.getFilePointer();
        logger.finest("About to read Audio starting from:" + startAudio);
        long startAudioWritten = rafTemp.getFilePointer();
        while (raf.getFilePointer() < raf.length()) {
            OggPageHeader nextPage = OggPageHeader.read(raf);
            ByteBuffer nextPageHeaderBuffer = ByteBuffer.allocate(nextPage.getRawHeaderData().length + nextPage.getPageLength());
            nextPageHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            nextPageHeaderBuffer.put(nextPage.getRawHeaderData());
            raf.getChannel().read(nextPageHeaderBuffer);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, ++pageSequence);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            crc = OggCRCFactory.computeCRC(nextPageHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                nextPageHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            nextPageHeaderBuffer.rewind();
            rafTemp.getChannel().write(nextPageHeaderBuffer);
        }
        if ((raf.length() - startAudio) != (rafTemp.length() - startAudioWritten)) {
            throw new CannotWriteException("File written counts dont match, file not written");
        }
    }
