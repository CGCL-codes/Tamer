    void encodeNotValidVersion(InputStream mockInputStream) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(".sff".getBytes()));
        final byte[] invalidVersion = new byte[] { 0, 0, 0, 2 };
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(invalidVersion));
    }
