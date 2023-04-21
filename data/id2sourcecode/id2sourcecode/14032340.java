    @Override
    public void streamBinaryTo(OutputStream os) throws IOException {
        try {
            int read = -1;
            byte data[] = new byte[READ_BUFFER_SIZE];
            while ((read = is.read(data)) > -1) {
                os.write(data, 0, read);
            }
        } finally {
            try {
                is.reset();
            } catch (IOException ioe) {
                LOG.error("Unable to reset stream: " + ioe.getMessage(), ioe);
            }
        }
    }
