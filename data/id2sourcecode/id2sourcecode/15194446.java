    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringWriter stringWriter = new StringWriter();
        int readCharacter = 0;
        while ((readCharacter = inputStream.read()) != -1) {
            stringWriter.write(readCharacter);
        }
        return stringWriter.toString();
    }
