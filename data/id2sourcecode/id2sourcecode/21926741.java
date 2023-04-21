    private byte[] applyFilter(String file_name, byte[] byte_buffer, Map filters) {
        logger.debug("XResourceManager._applyLoader() : filename = " + file_name);
        if (byte_buffer != null) {
            logger.debug("   resource-byte-size " + byte_buffer.length);
        }
        int first_dot = file_name.indexOf('.');
        if ((first_dot != -1) && (first_dot < file_name.length() - 1)) {
            String extension = file_name.substring(first_dot + 1, file_name.length());
            XResourceFilter filter = (XResourceFilter) (filters.get(extension));
            if (filter != null) {
                try {
                    ByteArrayInputStream byte_input = new ByteArrayInputStream(byte_buffer);
                    filter.setInputStream(byte_input);
                    ByteArrayOutputStream filtered_output = new ByteArrayOutputStream();
                    byte[] file_buffer = new byte[FILE_BUFFER_SIZE];
                    int bytes_read = filter.read(file_buffer);
                    while (bytes_read != -1) {
                        filtered_output.write(file_buffer, 0, bytes_read);
                        bytes_read = filter.read(file_buffer);
                    }
                    byte_buffer = filtered_output.toByteArray();
                } catch (java.io.IOException e) {
                    logger.error("Error on filtering resource " + e + ", within file " + file_name);
                }
            }
        }
        return byte_buffer;
    }
