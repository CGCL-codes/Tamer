    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        raf.seek(26);
        byte[] b = new byte[4];
        int pageSegments = raf.readByte() & 0xFF;
        raf.seek(0);
        b = new byte[27 + pageSegments];
        raf.read(b);
        OggPageHeader firstPage = new OggPageHeader(b);
        raf.seek(0);
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0, firstPage.getPageLength() + 27 + pageSegments);
        rafTemp.skipBytes((int) (firstPage.getPageLength() + raf.getFilePointer()));
        long pos = raf.getFilePointer();
        raf.seek(raf.getFilePointer() + 26);
        pageSegments = raf.readByte() & 0xFF;
        raf.seek(pos);
        b = new byte[27 + pageSegments];
        raf.read(b);
        OggPageHeader secondPage = new OggPageHeader(b);
        long secondPageEndPos = raf.getFilePointer();
        int oldCommentLength = 7;
        raf.seek(raf.getFilePointer() + 7);
        b = new byte[4];
        raf.read(b);
        int vendorStringLength = Utils.getNumber(b, 0, 3);
        oldCommentLength += 4 + vendorStringLength;
        raf.seek(raf.getFilePointer() + vendorStringLength);
        b = new byte[4];
        raf.read(b);
        int userComments = Utils.getNumber(b, 0, 3);
        oldCommentLength += 4;
        for (int i = 0; i < userComments; i++) {
            b = new byte[4];
            raf.read(b);
            int commentLength = Utils.getNumber(b, 0, 3);
            oldCommentLength += 4 + commentLength;
            raf.seek(raf.getFilePointer() + commentLength);
        }
        byte isValid = raf.readByte();
        oldCommentLength += 1;
        if (isValid != 1) throw new CannotWriteException("Unable to retreive old tag informations");
        ByteBuffer newComment = tc.convert(tag);
        int newCommentLength = newComment.capacity();
        int newSecondPageLength = secondPage.getPageLength() - oldCommentLength + newCommentLength;
        byte[] segmentTable = createSegmentTable(oldCommentLength, newCommentLength, secondPage);
        int newSecondPageHeaderLength = 27 + segmentTable.length;
        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        b = new String("OggS").getBytes();
        secondPageBuffer.put(b);
        secondPageBuffer.put((byte) 0);
        secondPageBuffer.put((byte) 0);
        secondPageBuffer.put(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 });
        int serialNb = secondPage.getSerialNumber();
        b = new byte[4];
        b[3] = (byte) ((serialNb & 0xFF000000) >> 24);
        b[2] = (byte) ((serialNb & 0x00FF0000) >> 16);
        b[1] = (byte) ((serialNb & 0x0000FF00) >> 8);
        b[0] = (byte) (serialNb & 0x000000FF);
        secondPageBuffer.put(b);
        int seqNb = secondPage.getPageSequence();
        b = new byte[4];
        b[3] = (byte) ((seqNb & 0xFF000000) >> 24);
        b[2] = (byte) ((seqNb & 0x00FF0000) >> 16);
        b[1] = (byte) ((seqNb & 0x0000FF00) >> 8);
        b[0] = (byte) (seqNb & 0x000000FF);
        secondPageBuffer.put(b);
        secondPageBuffer.put(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 });
        int crcOffset = 22;
        if (segmentTable.length > 255) {
            throw new CannotWriteException("In this special case we need to " + "create a new page, since we still hadn't the time for that " + "we won't write because it wouldn't create an ogg file.");
        }
        secondPageBuffer.put((byte) segmentTable.length);
        for (int i = 0; i < segmentTable.length; i++) secondPageBuffer.put(segmentTable[i]);
        secondPageBuffer.put(newComment);
        raf.seek(secondPageEndPos);
        raf.skipBytes(oldCommentLength);
        raf.getChannel().read(secondPageBuffer);
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++) {
            secondPageBuffer.put(crcOffset + i, crc[i]);
        }
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);
        rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getFilePointer(), raf.length() - raf.getFilePointer());
    }
