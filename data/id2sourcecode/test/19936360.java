    private static void putNumberAsArray(InputStream mockInputStream, long value, int maxNumberOfHexChars) throws IOException {
        String asHex = convertToPaddedHex(value, maxNumberOfHexChars);
        byte[] array = new byte[maxNumberOfHexChars / 2];
        for (int i = 0; i < maxNumberOfHexChars; i += 2) {
            String byteInHex = asHex.substring(i, i + 2);
            array[i / 2] = Integer.valueOf(byteInHex, 16).byteValue();
        }
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(maxNumberOfHexChars / 2))).andAnswer(writeArrayToInputStream(array));
    }
