    @Test
    public void invalidReadFailsMagicNumberShouldThrowSFFDecoderException() throws IOException {
        InputStream mockInputStream = createMock(InputStream.class);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(".ZTR".getBytes()));
        replay(mockInputStream);
        try {
            sut.decodeHeader(new DataInputStream(mockInputStream));
            fail("should wrap IOException in SFFDecoderException");
        } catch (IOException e) {
            SffDecoderException decoderException = (SffDecoderException) e.getCause();
            assertEquals("magic number does not match expected", decoderException.getMessage());
            assertNull(decoderException.getCause());
        }
        verify(mockInputStream);
    }
