    void encodeHeader(InputStream mockInputStream, SFFReadHeader readHeader) throws IOException {
        final String seqName = readHeader.getName();
        final int nameLength = seqName.length();
        int unpaddedLength = 16 + nameLength;
        final long padds = SFFUtil.caclulatePaddedBytes(unpaddedLength);
        putShort(mockInputStream, (short) (padds + unpaddedLength));
        putShort(mockInputStream, (short) nameLength);
        putInt(mockInputStream, readHeader.getNumberOfBases());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getLocalStart());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getLocalEnd());
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getLocalStart());
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getLocalEnd());
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(nameLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(seqName.getBytes()));
        expect(mockInputStream.skip(padds)).andReturn(padds);
    }
