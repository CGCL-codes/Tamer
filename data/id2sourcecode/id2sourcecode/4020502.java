    public static File writeEntireStreamToTemporaryFile(InputStream input, String fileName, String fileExtension) throws IOException {
        File temporaryFile = createTemporaryFileInDefaultTemporaryDirectory(fileName, fileExtension);
        OutputStream output = new FileOutputStream(temporaryFile);
        byte[] readCharacters = new byte[1];
        int readCharacterCount = input.read(readCharacters);
        while (readCharacterCount > 0) {
            output.write(readCharacters, 0, readCharacterCount);
            readCharacterCount = input.read(readCharacters);
        }
        output.close();
        input.close();
        return temporaryFile;
    }
