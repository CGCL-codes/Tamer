    @Test
    public void parseByteCountTwoBytes() throws IOException {
        InputStream in = createMock(InputStream.class);
        int expected = Short.MAX_VALUE + 1;
        byte[] asLittleEndian = IOUtil.switchEndian(IOUtil.convertUnsignedShortToByteArray(expected));
        expect(in.read()).andReturn(254);
        expect(in.read(isA(byte[].class), eq(0), eq(2))).andAnswer(EasyMockUtil.writeArrayToInputStream(asLittleEndian));
        replay(in);
        assertEquals(expected, CasUtil.parseByteCountFrom(in));
        verify(in);
    }
