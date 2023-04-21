    protected byte[] getEncodedImage() throws IOException {
        BufferedInputStream bis = null;
        if (input instanceof BufferedInputStream) bis = (BufferedInputStream) input; else bis = new BufferedInputStream(input);
        byte[] buffer = null;
        int bytes_read = 0;
        int bytes_available = 0;
        ByteArrayOutputStream baos = null;
        while ((bytes_available = bis.available()) > 0) {
            if (buffer != null) {
                if (baos == null) {
                    baos = new ByteArrayOutputStream(bytes_read + bytes_available);
                }
                baos.write(buffer, 0, bytes_read);
            }
            buffer = new byte[bytes_available];
            bytes_read = bis.read(buffer);
            if (bytes_read == -1) {
                break;
            }
        }
        if (baos != null) {
            if (bytes_read > 0) {
                baos.write(buffer, 0, bytes_read);
            }
            buffer = baos.toByteArray();
        }
        return buffer;
    }
