    public static String readerToString(Reader fr) throws Exception {
        try {
            StringWriter bos = new StringWriter();
            char[] buffer = new char[1024];
            int readCount = 0;
            while ((readCount = fr.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
            }
            return bos.toString();
        } finally {
            CloseUtils.close(fr);
        }
    }
