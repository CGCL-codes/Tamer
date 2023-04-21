    public void writeRemainingPages(int pageSequence, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        long startAudio = raf.getFilePointer();
        long startAudioWritten = rafTemp.getFilePointer();
        ByteBuffer bb = ByteBuffer.allocate((int) (raf.length() - raf.getFilePointer()));
        ByteBuffer bbTemp = ByteBuffer.allocate((int) (raf.length() - raf.getFilePointer()));
        raf.getChannel().read(bb);
        bb.rewind();
        while (bb.hasRemaining()) {
            OggPageHeader nextPage = OggPageHeader.read(bb);
            ByteBuffer nextPageHeaderBuffer = ByteBuffer.allocate(nextPage.getRawHeaderData().length + nextPage.getPageLength());
            nextPageHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            nextPageHeaderBuffer.put(nextPage.getRawHeaderData());
            ByteBuffer data = bb.slice();
            data.limit(nextPage.getPageLength());
            nextPageHeaderBuffer.put(data);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, ++pageSequence);
            calculateChecksumOverPage(nextPageHeaderBuffer);
            bb.position(bb.position() + nextPage.getPageLength());
            nextPageHeaderBuffer.rewind();
            bbTemp.put(nextPageHeaderBuffer);
        }
        bbTemp.rewind();
        rafTemp.getChannel().write(bbTemp);
        if ((raf.length() - startAudio) != (rafTemp.length() - startAudioWritten)) {
            throw new CannotWriteException("File written counts don't match, file not written");
        }
    }
