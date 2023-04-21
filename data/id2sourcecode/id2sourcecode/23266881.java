    public static <T extends OutputStream> T pump(InputStream is, T os, boolean closeIn, boolean closeOut) throws IOException {
        byte buff[] = new byte[1024];
        int len;
        Exception exception = null;
        try {
            while ((len = is.read(buff)) != -1) os.write(buff, 0, len);
        } catch (Exception ex) {
            exception = ex;
        } finally {
            try {
                try {
                    if (closeIn) is.close();
                } finally {
                    if (closeOut) os.close();
                }
            } catch (IOException ex) {
                if (exception != null) ex.printStackTrace(); else exception = ex;
            }
        }
        if (exception instanceof IOException) throw (IOException) exception; else if (exception instanceof RuntimeException) throw (RuntimeException) exception;
        return os;
    }
