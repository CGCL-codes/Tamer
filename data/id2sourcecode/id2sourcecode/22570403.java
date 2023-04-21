    @Test
    public void parseByteCountFourBytes() throws IOException {
        InputStream in = createMock(InputStream.class);
        long expected = Integer.MAX_VALUE + 1L;
        byte[] asLittleEndian = IOUtil.switchEndian(IOUtil.convertUnsignedIntToByteArray(expected));
        expect(in.read()).andReturn(255);
        expect(in.read(isA(byte[].class), eq(0), eq(4))).andAnswer(EasyMockUtil.writeArrayToInputStream(asLittleEndian));
        replay(in);
        assertEquals(expected, CasUtil.parseByteCountFrom(in));
        verify(in);
    }
