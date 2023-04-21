    void encodeHeaderWithWrongSequenceLength(InputStream mockInputStream, SffReadHeader readHeader) throws IOException {
        final String seqName = readHeader.getId();
        final int nameLength = seqName.length();
        int unpaddedLength = 16 + nameLength;
        final long padds = SffUtil.caclulatePaddedBytes(unpaddedLength);
        putShort(mockInputStream, (short) (padds + unpaddedLength));
        putShort(mockInputStream, (short) (nameLength + 1));
        putInt(mockInputStream, readHeader.getNumberOfBases());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getBegin());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getEnd());
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getBegin());
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getEnd());
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(nameLength + 1))).andAnswer(EasyMockUtil.writeArrayToInputStream(seqName.getBytes()));
        expect(mockInputStream.read(isA(byte[].class), eq(13), eq(1))).andReturn(-1);
    }
