    private static void writeToFile(File file, InputStream inStr) throws IOException {
        writeToFile(file, readToString(inStr));
    }
