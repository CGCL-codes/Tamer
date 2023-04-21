    public static byte[] readFileToByteArray(File file) throws IOException {
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException(String.format("Cannot read file \"%s\": %d bytes is way too much", file.getName(), length));
        }
        byte[] bytes = new byte[(int) length];
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) > 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException(String.format("Cannot read file \"%s\": read only %d bytes of %d", file.getName(), offset, length));
            }
            return bytes;
        } finally {
            silentClose(is);
        }
    }
