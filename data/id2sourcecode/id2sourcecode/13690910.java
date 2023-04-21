    @Test
    public void readCasUnsignedByteShortValue() throws IOException {
        InputStream in = createMock(InputStream.class);
        short expected = Byte.MAX_VALUE + 1;
        byte[] asLittleEndian = IOUtil.switchEndian(IOUtil.convertUnsignedByteToByteArray(expected));
        expect(in.read(isA(byte[].class), eq(0), eq(1))).andAnswer(EasyMockUtil.writeArrayToInputStream(asLittleEndian));
        replay(in);
        assertEquals(expected, CasUtil.readCasUnsignedByte(in));
        verify(in);
    }
