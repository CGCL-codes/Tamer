    private byte[] _loadResource(String path) {
        logger.debug("XResourceManager._loadFileResource() : path = " + path);
        byte[] byte_buffer = null;
        try {
            BufferedInputStream input = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
            ByteArrayOutputStream byte_output = new ByteArrayOutputStream();
            byte[] file_buffer = new byte[FILE_BUFFER_SIZE];
            int bytes_read = input.read(file_buffer);
            while (bytes_read != -1) {
                byte_output.write(file_buffer, 0, bytes_read);
                bytes_read = input.read(file_buffer);
            }
            byte_buffer = byte_output.toByteArray();
        } catch (Exception e) {
            logger.error("Error on loading form resource: " + path, e);
        }
        byte_buffer = applyFilter(path, byte_buffer, onLoadFilters);
        logger.debug("/XResourceManager._loadFileResource()");
        return byte_buffer;
    }
