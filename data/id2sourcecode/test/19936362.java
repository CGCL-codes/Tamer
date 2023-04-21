    public static void putUnSignedLong(InputStream mockInputStream, BigInteger unsignedLong) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(8))).andAnswer(writeArrayToInputStream(IOUtil.convertUnsignedLongToByteArray(unsignedLong)));
    }
