    @Test
    public void readLengthNotEnoughBytesReadShouldWrapInTraceDecoderException() throws IOException {
        byte[] tooSmall = new byte[] { 1, 2, 3 };
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(tooSmall));
        expect(mockInputStream.read(isA(byte[].class), eq(3), eq(1))).andReturn(-1);
        replay(mockInputStream);
        try {
            sut.readLength(mockInputStream);
            fail("should throw TraceDecoderException when too small");
        } catch (TraceDecoderException e) {
            assertEquals("error reading chunk length", e.getMessage());
            TraceDecoderException cause = (TraceDecoderException) e.getCause();
            assertEquals("invalid metaData length", cause.getMessage());
        }
        verify(mockInputStream);
    }
