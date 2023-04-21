    private void replaceSecondPageOnly(int oldCommentLength, int setupHeaderLength, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, long secondPageHeaderEndPos, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException {
        byte[] segmentTable = createSegmentTable(newCommentLength, setupHeaderLength);
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        secondPageBuffer.order(ByteOrder.LITTLE_ENDIAN);
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
        secondPageBuffer.put((byte) segmentTable.length);
        for (int i = 0; i < segmentTable.length; i++) {
            secondPageBuffer.put(segmentTable[i]);
        }
        secondPageBuffer.put(newComment);
        raf.seek(secondPageHeaderEndPos);
        raf.skipBytes(oldCommentLength);
        raf.getChannel().read(secondPageBuffer);
        secondPageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++) {
            secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);
        rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getFilePointer(), raf.length() - raf.getFilePointer());
    }
