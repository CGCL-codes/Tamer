    public static <T extends Writer> T pump(Reader reader, T writer, boolean closeReader, boolean closeWriter) throws IOException {
        char buff[] = new char[1024];
        int len;
        Exception exception = null;
        try {
            while ((len = reader.read(buff)) != -1) writer.write(buff, 0, len);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            try {
                try {
                    if (closeReader) reader.close();
                } finally {
                    if (closeWriter) writer.close();
                }
            } catch (IOException ex) {
                if (exception != null) ex.printStackTrace(); else exception = ex;
            }
        }
        if (exception instanceof IOException) throw (IOException) exception; else if (exception instanceof RuntimeException) throw (RuntimeException) exception;
        return writer;
    }
