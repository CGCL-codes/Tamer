    private File downloadResource(Resource resource) throws IOException {
        File tempFile = File.createTempFile(resource.getSimpleName(), null);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "JRJSF_0035", new Object[] { resource.getLocation(), tempFile });
        }
        tempFile.createNewFile();
        InputStream is = resource.getInputStream();
        OutputStream os = new FileOutputStream(tempFile);
        try {
            int read;
            byte[] buff = new byte[BUFFER_SIZE];
            while (0 < (read = is.read(buff))) {
                os.write(buff, 0, read);
            }
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                ;
            }
            try {
                os.close();
                os = null;
            } catch (IOException e) {
                ;
            }
        }
        return tempFile;
    }
