    private void replaceSecondPageAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        logger.fine("WriteOgg Type 2");
        ByteBuffer secondPageBuffer = startCreateBasicSecondPage(originalHeaderSizes, newCommentLength, newSecondPageLength, secondPageHeader, newComment);
        int pageSequence = secondPageHeader.getPageSequence();
        byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacketAndAdditionalPackets(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
        logger.finest(setupHeaderData.length + ":" + secondPageBuffer.position() + ":" + secondPageBuffer.capacity());
        secondPageBuffer.put(setupHeaderData);
        calculateChecksumOverPage(secondPageBuffer);
        rafTemp.getChannel().write(secondPageBuffer);
        writeRemainingPages(pageSequence, raf, rafTemp);
    }
