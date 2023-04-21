    private byte[] readBytesFromFile(InputStream fileInputStream) throws IOException {
        ByteArrayOutputStream byte_output = new ByteArrayOutputStream();
        byte[] file_buffer = new byte[4096];
        int bytes_read = fileInputStream.read(file_buffer);
        while (bytes_read != -1) {
            byte_output.write(file_buffer, 0, bytes_read);
            bytes_read = fileInputStream.read(file_buffer);
        }
        byte_output.flush();
        return byte_output.toByteArray();
    }
