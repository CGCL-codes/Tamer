    private void writeToOutputStream(OutputStream out, InputStream is, Decrypt xor) throws IOException {
        int readByte;
        byte[] buffer = new byte[Helper.BUFFER_SIZE];
        while ((readByte = is.read(buffer)) > 0) {
            if (xor != null) {
                buffer = xor.decrypt(buffer, readByte);
            }
            out.write(buffer, 0, readByte);
        }
    }
