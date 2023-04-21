    @Test
    public void readCasUnsignedByteByteValue() throws IOException {
        InputStream in = createMock(InputStream.class);
        short expected = 120;
        byte[] asLittleEndian = IOUtil.switchEndian(IOUtil.convertUnsignedByteToByteArray(expected));
        expect(in.read(isA(byte[].class), eq(0), eq(1))).andAnswer(EasyMockUtil.writeArrayToInputStream(asLittleEndian));
        replay(in);
        assertEquals(expected, CasUtil.readCasUnsignedByte(in));
        verify(in);
    }
