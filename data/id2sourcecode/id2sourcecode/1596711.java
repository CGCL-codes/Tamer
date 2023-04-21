    public static String readFileToString(File pSrcFile) throws IOException {
        BufferedReader tReader = null;
        StringWriter tWriter = null;
        UnicodeReader tUnicodeReader = new UnicodeReader(new FileInputStream(pSrcFile), null);
        char[] tBuffer = new char[16 * 1024];
        int read;
        try {
            tReader = new BufferedReader(tUnicodeReader);
            tWriter = new StringWriter();
            while ((read = tReader.read(tBuffer)) != -1) {
                tWriter.write(tBuffer, 0, read);
            }
            tWriter.flush();
            return tWriter.toString();
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                tWriter.close();
                tReader.close();
                tUnicodeReader.close();
            } catch (Exception ex) {
            }
        }
    }
