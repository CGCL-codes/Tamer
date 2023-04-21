    @Test
    public void readLengthNotEnoughBytesReadShouldWrapInTraceDecoderException() throws IOException {
        byte[] tooSmall = new byte[] { 1, 2, 3 };
        expect(mockInputStream.read(isA(byte[].class))).andAnswer(EasyMockUtil.writeArrayToInputStream(tooSmall));
        replay(mockInputStream);
        try {
            sut.readLength(mockInputStream);
            fail("should throw TraceDecoderException when too small");
        } catch (TraceDecoderException e) {
            assertEquals("error reading chunk length", e.getMessage());
            TraceDecoderException cause = (TraceDecoderException) e.getCause();
            assertEquals("invalid metaData length record only has 3 bytes", cause.getMessage());
        }
        verify(mockInputStream);
    }
