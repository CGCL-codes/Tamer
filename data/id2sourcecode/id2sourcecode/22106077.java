    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException {
        logger.info("Starting to write file:");
        logger.fine("Read identificationHeader:");
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        raf.seek(0);
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0, pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        rafTemp.skipBytes(pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        logger.fine("Written identificationHeader:");
        logger.fine("Read 2ndpage:");
        OggPageHeader secondPageHeader = OggPageHeader.read(raf);
        long secondPageHeaderEndPos = raf.getFilePointer();
        raf.seek(0);
        OggVorbisTagReader.OggVorbisHeaderSizes vorbisHeaderSizes = reader.readOggVorbisHeaderSizes(raf);
        ByteBuffer newComment = tc.convert(tag);
        int newCommentLength = newComment.capacity();
        int setupHeaderLength = vorbisHeaderSizes.getSetupHeaderSize();
        int oldCommentLength = vorbisHeaderSizes.getCommentHeaderSize();
        int newSecondPageLength = setupHeaderLength + newCommentLength;
        logger.fine("Old Page size: " + secondPageHeader.getPageLength());
        logger.fine("Setup Header Size: " + setupHeaderLength);
        logger.fine("Old comment: " + oldCommentLength);
        logger.fine("New comment: " + newCommentLength);
        logger.fine("New Page Size: " + newSecondPageLength);
        if (isCommentAndSetupHeaderFitsOnASinglePage(newCommentLength, setupHeaderLength)) {
            if ((secondPageHeader.getPageLength() < OggPageHeader.MAXIMUM_PAGE_DATA_SIZE) && (secondPageHeader.getPacketList().size() == 2) && (!secondPageHeader.isLastPacketIncomplete())) {
                logger.info("Header and Setup remain on single page:");
                replaceSecondPageOnly(oldCommentLength, setupHeaderLength, newCommentLength, newSecondPageLength, secondPageHeader, newComment, secondPageHeaderEndPos, raf, rafTemp);
            } else {
                logger.info("Header and Setup now on single page:");
                replaceSecondPageAndRenumberPageSeqs(vorbisHeaderSizes, newCommentLength, newSecondPageLength, secondPageHeader, newComment, raf, rafTemp);
            }
        } else {
            logger.info("Header and Setup remain on multiple page:");
            replacePagesAndRenumberPageSeqs(vorbisHeaderSizes, newCommentLength, secondPageHeader, newComment, raf, rafTemp);
        }
    }
