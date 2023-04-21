    private void replaceSecondPageOnly(OggVorbisTagReader.OggVorbisHeaderSizes vorbisHeaderSizes, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, long secondPageHeaderEndPos, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException {
        logger.fine("WriteOgg Type 1");
        ByteBuffer secondPageBuffer = startCreateBasicSecondPage(vorbisHeaderSizes, newCommentLength, newSecondPageLength, secondPageHeader, newComment);
        raf.seek(secondPageHeaderEndPos);
        raf.skipBytes(vorbisHeaderSizes.getCommentHeaderSize());
        raf.getChannel().read(secondPageBuffer);
        calculateChecksumOverPage(secondPageBuffer);
        rafTemp.getChannel().write(secondPageBuffer);
        rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getFilePointer(), raf.length() - raf.getFilePointer());
    }
