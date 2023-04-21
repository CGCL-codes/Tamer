    public static void putUnSignedInt(InputStream mockInputStream, long unsignedInt) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(4))).andAnswer(writeArrayToInputStream(IOUtil.convertUnsignedIntToByteArray(unsignedInt)));
    }
