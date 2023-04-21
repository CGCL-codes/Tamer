    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException {
        logger.info("Starting to write file:");
        logger.fine("Read 1st Page:identificationHeader:");
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        raf.seek(0);
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0, pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        rafTemp.skipBytes(pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        logger.fine("Written identificationHeader:");
        OggPageHeader secondPageHeader = OggPageHeader.read(raf);
        long secondPageHeaderEndPos = raf.getFilePointer();
        logger.fine("Read 2nd Page:comment and setup and possibly audio:Header finishes at file position:" + secondPageHeaderEndPos);
        raf.seek(0);
        OggVorbisTagReader.OggVorbisHeaderSizes vorbisHeaderSizes = reader.readOggVorbisHeaderSizes(raf);
        ByteBuffer newComment = tc.convert(tag);
        int newCommentLength = newComment.capacity();
        int newSecondPageDataLength = vorbisHeaderSizes.getSetupHeaderSize() + newCommentLength + vorbisHeaderSizes.getExtraPacketDataSize();
        logger.fine("Old 2nd Page no of packets: " + secondPageHeader.getPacketList().size());
        logger.fine("Old 2nd Page size: " + secondPageHeader.getPageLength());
        logger.fine("Old last packet incomplete: " + secondPageHeader.isLastPacketIncomplete());
        logger.fine("Setup Header Size: " + vorbisHeaderSizes.getSetupHeaderSize());
        logger.fine("Extra Packets: " + vorbisHeaderSizes.getExtraPacketList().size());
        logger.fine("Extra Packet Data Size: " + vorbisHeaderSizes.getExtraPacketDataSize());
        logger.fine("Old comment: " + vorbisHeaderSizes.getCommentHeaderSize());
        logger.fine("New comment: " + newCommentLength);
        logger.fine("New Page Data Size: " + newSecondPageDataLength);
        if (isCommentAndSetupHeaderFitsOnASinglePage(newCommentLength, vorbisHeaderSizes.getSetupHeaderSize(), vorbisHeaderSizes.getExtraPacketList())) {
            if ((secondPageHeader.getPageLength() < OggPageHeader.MAXIMUM_PAGE_DATA_SIZE) && (((secondPageHeader.getPacketList().size() == 2) && (!secondPageHeader.isLastPacketIncomplete())) || (secondPageHeader.getPacketList().size() > 2))) {
                logger.fine("Header and Setup remain on single page:");
                replaceSecondPageOnly(vorbisHeaderSizes, newCommentLength, newSecondPageDataLength, secondPageHeader, newComment, secondPageHeaderEndPos, raf, rafTemp);
            } else {
                logger.fine("Header and Setup now on single page:");
                replaceSecondPageAndRenumberPageSeqs(vorbisHeaderSizes, newCommentLength, newSecondPageDataLength, secondPageHeader, newComment, raf, rafTemp);
            }
        } else {
            logger.fine("Header and Setup with shift audio:");
            replacePagesAndRenumberPageSeqs(vorbisHeaderSizes, newCommentLength, secondPageHeader, newComment, raf, rafTemp);
        }
    }
