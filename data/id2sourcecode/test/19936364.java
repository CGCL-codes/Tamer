    public static void putUnSignedShort(InputStream mockInputStream, int unsignedShort) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(2))).andAnswer(writeArrayToInputStream(IOUtil.convertUnsignedShortToByteArray(unsignedShort)));
    }
