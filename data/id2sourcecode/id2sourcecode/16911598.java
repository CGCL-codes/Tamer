    private void writeMetaData(ByteArrayOutputStream baos) throws UnsupportedEncodingException, IOException {
        if (this.metadata == null) {
            return;
        }
        for (Iterator i = this.metadata.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof String) {
                baos.write(((String) obj).getBytes(DEFAULT_ENCODING));
            } else if (obj instanceof File) {
                InputStream is = null;
                try {
                    is = new BufferedInputStream(new FileInputStream((File) obj));
                    byte[] buffer = new byte[4096];
                    for (int read = -1; (read = is.read(buffer)) != -1; ) {
                        baos.write(buffer, 0, read);
                    }
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } else if (obj != null) {
                logger.severe("Unsupported metadata type: " + obj);
            }
        }
        return;
    }
