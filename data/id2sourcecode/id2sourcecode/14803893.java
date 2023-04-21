    protected File readIntoTempFile(InputStream source) throws IOException {
        File tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
        tempFile.deleteOnExit();
        InputStreamReader in = new InputStreamReader(source);
        FileWriter out = new FileWriter(tempFile);
        char[] buf = new char[TEMP_FILE_BUFSIZ];
        int len;
        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        in.close();
        in = null;
        out.close();
        out = null;
        return tempFile;
    }
