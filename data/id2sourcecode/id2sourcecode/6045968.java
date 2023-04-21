    private byte[] convertUTF16StringToLittleEndian(byte[] bytesString) {
        if (bytesString == null) {
            return new byte[0];
        }
        for (int i = 0; i < bytesString.length; i += 2) {
            byte tmp = bytesString[i];
            bytesString[i] = bytesString[i + 1];
            bytesString[i + 1] = tmp;
        }
        return bytesString;
    }
