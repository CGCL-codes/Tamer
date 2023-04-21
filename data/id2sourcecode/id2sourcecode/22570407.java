    @Test
    public void readCasUnsignedLongBigIntValue() throws IOException {
        InputStream in = createMock(InputStream.class);
        BigInteger expected = new BigInteger("18446744073709551614");
        byte[] asLittleEndian = IOUtil.switchEndian(IOUtil.convertUnsignedLongToByteArray(expected));
        expect(in.read(isA(byte[].class), eq(0), eq(8))).andAnswer(EasyMockUtil.writeArrayToInputStream(asLittleEndian));
        replay(in);
        assertEquals(expected, CasUtil.readCasUnsignedLong(in));
        verify(in);
    }
