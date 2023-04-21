    @Test
    public void readLength() throws TraceDecoderException, IOException {
        int length = 20;
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(length);
        expect(mockInputStream.read(isA(byte[].class))).andAnswer(EasyMockUtil.writeArrayToInputStream(buf.array()));
        replay(mockInputStream);
        assertEquals(length, sut.readLength(mockInputStream));
        verify(mockInputStream);
    }
