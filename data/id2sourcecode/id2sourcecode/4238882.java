    void encodeHeaderWithWrongSequenceLength(InputStream mockInputStream, SFFReadHeader readHeader) throws IOException {
        final String seqName = readHeader.getName();
        final int nameLength = seqName.length();
        int unpaddedLength = 16 + nameLength;
        final long padds = SFFUtil.caclulatePaddedBytes(unpaddedLength);
        putShort(mockInputStream, (short) (padds + unpaddedLength));
        putShort(mockInputStream, (short) (nameLength + 1));
        putInt(mockInputStream, readHeader.getNumberOfBases());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getStart());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getEnd());
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getStart());
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getEnd());
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(nameLength + 1))).andAnswer(EasyMockUtil.writeArrayToInputStream(seqName.getBytes()));
    }
