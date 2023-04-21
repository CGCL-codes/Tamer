    private File createFile(byte[] contents) throws JiraException {
        File tempFile;
        try {
            tempFile = File.createTempFile("_ra", "att");
        } catch (IOException ex) {
            log.error("createFile - ", ex);
            throw new JiraException("Unable to create temporary attachement file.");
        }
        FileOutputStream os = null;
        InputStream is = null;
        try {
            os = new FileOutputStream(tempFile);
            is = new ByteArrayInputStream(contents);
            byte[] b = new byte[4096];
            while (true) {
                int read = is.read(b);
                if (read <= 0) {
                    break;
                }
                os.write(b, 0, read);
            }
            return tempFile;
        } catch (IOException ex) {
            log.error("createFile - ", ex);
            throw new JiraException("Unable to create temporary attachement file [" + tempFile.getAbsolutePath() + "].");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    log.warn("createFile - ", ex);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    log.warn("createFile - ", ex);
                }
            }
        }
    }
