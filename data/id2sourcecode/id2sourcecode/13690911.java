    @Test
    public void readCasUnsignedLongLongValue() throws IOException {
        InputStream in = createMock(InputStream.class);
        BigInteger expected = new BigInteger(Long.valueOf(Long.MAX_VALUE).toString());
        byte[] asLittleEndian = IOUtil.switchEndian(IOUtil.convertUnsignedLongToByteArray(expected));
        expect(in.read(isA(byte[].class), eq(0), eq(8))).andAnswer(EasyMockUtil.writeArrayToInputStream(asLittleEndian));
        replay(in);
        assertEquals(expected, CasUtil.readCasUnsignedLong(in));
        verify(in);
    }
