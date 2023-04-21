    private void replacePagesAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes, int newCommentLength, OggPageHeader secondPageHeader, ByteBuffer newComment, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        int pageSequence = secondPageHeader.getPageSequence();
        int noOfCompletePagesNeededForComment = newCommentLength / OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.info("Comment requires:" + noOfCompletePagesNeededForComment + " complete pages");
        int newCommentOffset = 0;
        if (noOfCompletePagesNeededForComment > 0) {
            for (int i = 0; i < noOfCompletePagesNeededForComment; i++) {
                byte[] segmentTable = this.createSegments(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE, false);
                int pageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
                ByteBuffer pageBuffer = ByteBuffer.allocate(pageHeaderLength + OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
                pageBuffer.order(ByteOrder.LITTLE_ENDIAN);
                pageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
                pageBuffer.put((byte) segmentTable.length);
                for (byte aSegmentTable : segmentTable) {
                    pageBuffer.put(aSegmentTable);
                }
                ByteBuffer nextPartOfComment = newComment.slice();
                nextPartOfComment.limit(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
                pageBuffer.put(nextPartOfComment);
                pageBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
                pageSequence++;
                if (i != 0) {
                    pageBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
                }
                calculateChecksumOverPage(pageBuffer);
                rafTemp.getChannel().write(pageBuffer);
                newCommentOffset += OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
                newComment.position(newCommentOffset);
            }
        }
        int lastPageCommentPacketSize = newCommentLength % OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.fine("Last comment packet size:" + lastPageCommentPacketSize);
        if (!isCommentAndSetupHeaderFitsOnASinglePage(lastPageCommentPacketSize, originalHeaderSizes.getSetupHeaderSize(), originalHeaderSizes.getExtraPacketList())) {
            logger.fine("WriteOgg Type 3");
            {
                byte[] segmentTable = createSegments(lastPageCommentPacketSize, true);
                int pageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
                ByteBuffer pageBuffer = ByteBuffer.allocate(lastPageCommentPacketSize + pageHeaderLength);
                pageBuffer.order(ByteOrder.LITTLE_ENDIAN);
                pageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
                pageBuffer.put((byte) segmentTable.length);
                for (byte aSegmentTable : segmentTable) {
                    pageBuffer.put(aSegmentTable);
                }
                newComment.position(newCommentOffset);
                pageBuffer.put(newComment.slice());
                pageBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
                if (noOfCompletePagesNeededForComment > 0) {
                    pageBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
                }
                logger.fine("Writing Last Comment Page " + pageSequence + " to file");
                pageSequence++;
                calculateChecksumOverPage(pageBuffer);
                rafTemp.getChannel().write(pageBuffer);
            }
            {
                byte[] segmentTable = this.createSegmentTable(originalHeaderSizes.getSetupHeaderSize(), originalHeaderSizes.getExtraPacketList());
                int pageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
                byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacketAndAdditionalPackets(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
                ByteBuffer pageBuffer = ByteBuffer.allocate(setupHeaderData.length + pageHeaderLength);
                pageBuffer.order(ByteOrder.LITTLE_ENDIAN);
                pageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
                pageBuffer.put((byte) segmentTable.length);
                for (byte aSegmentTable : segmentTable) {
                    pageBuffer.put(aSegmentTable);
                }
                pageBuffer.put(setupHeaderData);
                pageBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
                logger.fine("Writing Setup Header and packets Page " + pageSequence + " to file");
                calculateChecksumOverPage(pageBuffer);
                rafTemp.getChannel().write(pageBuffer);
            }
        } else {
            logger.fine("WriteOgg Type 4");
            int newSecondPageDataLength = originalHeaderSizes.getSetupHeaderSize() + lastPageCommentPacketSize + originalHeaderSizes.getExtraPacketDataSize();
            newComment.position(newCommentOffset);
            ByteBuffer lastComment = newComment.slice();
            ByteBuffer lastHeaderBuffer = startCreateBasicSecondPage(originalHeaderSizes, lastPageCommentPacketSize, newSecondPageDataLength, secondPageHeader, lastComment);
            raf.seek(originalHeaderSizes.getSetupHeaderStartPosition());
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacketAndAdditionalPackets(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
            lastHeaderBuffer.put(setupHeaderData);
            lastHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            calculateChecksumOverPage(lastHeaderBuffer);
            rafTemp.getChannel().write(lastHeaderBuffer);
        }
        writeRemainingPages(pageSequence, raf, rafTemp);
    }
