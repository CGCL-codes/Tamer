    public static File writeStreamToFile(InputStream input, File outputFile) throws IOException {
        OutputStream output = new FileOutputStream(outputFile);
        byte[] readCharacters = new byte[1];
        int readCharacterCount = input.read(readCharacters);
        while (readCharacterCount > 0) {
            output.write(readCharacters, 0, readCharacterCount);
            readCharacterCount = input.read(readCharacters);
        }
        output.close();
        input.close();
        return outputFile;
    }
