    @Test
    public void incorrectNumberOfBytesReadShouldThrowSectionDecoderException() throws IOException {
        byte[] only4Bytes = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };
        expect(mockHeader.getPrivateDataOffset()).andReturn(0);
        final int expectedNumberOfBytes = only4Bytes.length + 1;
        expect(mockHeader.getPrivateDataSize()).andReturn(expectedNumberOfBytes);
        InputStream mockInputStream = createMock(InputStream.class);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(expectedNumberOfBytes))).andAnswer(EasyMockUtil.writeArrayToInputStream(only4Bytes));
        replay(mockHeader, mockInputStream);
        try {
            sut.decode(new DataInputStream(mockInputStream), 0L, mockHeader, c);
            fail("should throw exception if not expected number of bytes read");
        } catch (IOException e) {
            SectionDecoderException decoderException = (SectionDecoderException) e.getCause();
            assertEquals("could not read entire private data section", decoderException.getMessage());
        }
    }
