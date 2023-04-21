    void encodeHeader(InputStream mockInputStream, SffReadHeader readHeader) throws IOException {
        final String seqName = readHeader.getId();
        final int nameLength = seqName.length();
        int unpaddedLength = 16 + nameLength;
        final long padds = SffUtil.caclulatePaddedBytes(unpaddedLength);
        putShort(mockInputStream, (short) (padds + unpaddedLength));
        putShort(mockInputStream, (short) nameLength);
        putInt(mockInputStream, readHeader.getNumberOfBases());
        putShort(mockInputStream, (short) readHeader.getQualityClip().getBegin(CoordinateSystem.RESIDUE_BASED));
        putShort(mockInputStream, (short) readHeader.getQualityClip().getEnd(CoordinateSystem.RESIDUE_BASED));
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getBegin(CoordinateSystem.RESIDUE_BASED));
        putShort(mockInputStream, (short) readHeader.getAdapterClip().getEnd(CoordinateSystem.RESIDUE_BASED));
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(nameLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(seqName.getBytes()));
        expect(mockInputStream.skip(padds)).andReturn(padds);
    }
