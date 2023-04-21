    void invailidFlow(InputStream mockInputStream, SffCommonHeader header) throws IOException {
        final short keyLength = (short) (header.getKeySequence().getLength());
        int size = 31 + header.getNumberOfFlowsPerRead() + keyLength;
        long padding = SffUtil.caclulatePaddedBytes(size);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(".sff".getBytes()));
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(new byte[] { 0, 0, 0, 1 }));
        EasyMockUtil.putUnSignedLong(mockInputStream, header.getIndexOffset());
        EasyMockUtil.putUnSignedInt(mockInputStream, header.getIndexLength());
        EasyMockUtil.putUnSignedInt(mockInputStream, header.getNumberOfReads());
        EasyMockUtil.putUnSignedShort(mockInputStream, (short) (size + padding));
        EasyMockUtil.putUnSignedShort(mockInputStream, keyLength);
        EasyMockUtil.putUnSignedShort(mockInputStream, header.getNumberOfFlowsPerRead());
        EasyMockUtil.putByte(mockInputStream, (byte) 1);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(header.getNumberOfFlowsPerRead()))).andAnswer(EasyMockUtil.writeArrayToInputStream(header.getFlowSequence().toString().substring(1).getBytes()));
        expect(mockInputStream.read(isA(byte[].class), eq(11), eq(1))).andReturn(-1);
    }
