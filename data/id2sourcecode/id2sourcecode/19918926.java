    public static byte[] getContents(File file) throws JiraException {
        ByteArrayOutputStream os = null;
        InputStream is = null;
        try {
            os = new ByteArrayOutputStream();
            is = new FileInputStream(file);
            byte[] b = new byte[4096];
            while (true) {
                int read = is.read(b);
                if (read <= 0) {
                    break;
                }
                os.write(b, 0, read);
            }
            return os.toByteArray();
        } catch (IOException ex) {
            log.error("getContents - ", ex);
            throw new JiraException("Unable to get contents of file [" + file.getAbsolutePath() + "].");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    log.warn("getContents - Unable to close input stream.", ex);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    log.warn("getContents - Unable to close output stream.", ex);
                }
            }
        }
    }
