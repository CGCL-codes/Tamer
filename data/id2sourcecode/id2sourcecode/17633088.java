    private String getFileContents(File fsrc) throws Exception {
        StringWriter bos = new StringWriter();
        Reader fr = getReaderForFile(fsrc);
        char[] buffer = new char[1024];
        int readCount = 0;
        while ((readCount = fr.read(buffer)) > 0) {
            bos.write(buffer, 0, readCount);
        }
        fr.close();
        return bos.toString();
    }
