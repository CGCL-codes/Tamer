    @Test
    public void readMetaData() throws IOException, TraceDecoderException {
        int lengthToSkip = 1234;
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(lengthToSkip);
        expect(mockInputStream.read(isA(byte[].class))).andAnswer(EasyMockUtil.writeArrayToInputStream(buf.array()));
        expect(mockInputStream.skip(lengthToSkip)).andReturn((long) lengthToSkip);
        replay(mockInputStream);
        sut.readMetaData(mockInputStream);
        verify(mockInputStream);
    }
