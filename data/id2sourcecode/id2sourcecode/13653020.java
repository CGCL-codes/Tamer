    public InputStream load() throws IOException {
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();
        encoding = connection.getContentEncoding();
        LOGGER.debug("open connection to " + url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
        byte[] buf = new byte[BUFFER_SIZE];
        try {
            int len = 0;
            do {
                len = input.read(buf);
                LOGGER.debug("read " + len + " bytes");
                if (len > 0) baos.write(buf, 0, len);
            } while (len > 0);
        } catch (IOException io) {
            LOGGER.debug(io);
        }
        data = baos.toByteArray();
        return new ByteArrayInputStream(data);
    }
