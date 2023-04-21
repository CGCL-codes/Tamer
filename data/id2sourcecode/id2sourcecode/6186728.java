    public static String getTextContents(Reader fr, boolean closeWhenDone) throws Exception {
        StringWriter bos = new StringWriter();
        char[] buffer = new char[1024];
        int readCount = 0;
        while ((readCount = fr.read(buffer)) > 0) {
            bos.write(buffer, 0, readCount);
        }
        if (closeWhenDone) {
            fr.close();
        }
        return bos.toString();
    }
