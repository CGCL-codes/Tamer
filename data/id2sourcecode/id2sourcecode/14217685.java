    @Test
    public void readMetaDataSkipThrowsException() throws IOException {
        int lengthToSkip = 1234;
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(lengthToSkip);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(buf.array()));
        expect(mockInputStream.skip(lengthToSkip)).andThrow(expectedException);
        replay(mockInputStream);
        try {
            sut.readMetaData(mockInputStream);
            fail("should wrap IOException in TraceDecoderException");
        } catch (TraceDecoderException e) {
            assertEquals("error reading chunk meta data", e.getMessage());
            assertEquals(expectedException, e.getCause());
        }
        verify(mockInputStream);
    }
