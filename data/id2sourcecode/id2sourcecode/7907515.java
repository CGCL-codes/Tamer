    public static String readWord(InputStream source) throws IOException {
        tmpWord.reset();
        int readByte = -1;
        while (Utils.isWhiteSpace((readByte = source.read()))) ;
        tmpWord.write(readByte);
        while (!Utils.isWhiteSpace((readByte = source.read()))) tmpWord.write(readByte);
        return new String(tmpWord.toByteArray());
    }
