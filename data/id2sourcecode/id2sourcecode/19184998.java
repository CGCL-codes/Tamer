    public static StaticBody fromStream(final InputStream inStream) throws BOSHException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            do {
                read = inStream.read(buffer);
                if (read > 0) {
                    byteOut.write(buffer, 0, read);
                }
            } while (read >= 0);
        } catch (IOException iox) {
            throw (new BOSHException("Could not read body data", iox));
        }
        return fromString(byteOut.toString());
    }
