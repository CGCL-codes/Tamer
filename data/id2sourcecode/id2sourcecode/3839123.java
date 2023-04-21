    @Test
    public void validWithSkip() throws SectionDecoderException, IOException {
        InputStream mockInputStream = createMock(InputStream.class);
        int bytesToSkip = 100;
        expect(mockInputStream.skip(bytesToSkip)).andReturn((long) bytesToSkip);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(data.length))).andAnswer(EasyMockUtil.writeArrayToInputStream(data));
        replay(mockInputStream);
        decodeValid(new DataInputStream(mockInputStream), 0, bytesToSkip);
        verify(mockInputStream);
    }
