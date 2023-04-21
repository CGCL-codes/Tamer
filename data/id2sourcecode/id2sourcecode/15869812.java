    public void writeRemainingPagesOld(int pageSequence, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
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
            calculateChecksumOverPage(nextPageHeaderBuffer);
            rafTemp.getChannel().write(nextPageHeaderBuffer);
        }
        if ((raf.length() - startAudio) != (rafTemp.length() - startAudioWritten)) {
            throw new CannotWriteException("File written counts don't match, file not written");
        }
    }
