    public static final byte[] decodeBytes(String str) throws IOException {
        try {
            ByteArrayInputStream encodedStringStream = new ByteArrayInputStream(str.getBytes());
            InputStream decoder = MimeUtility.decode(encodedStringStream, "base64");
            ByteArrayOutputStream decodedByteStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            while (true) {
                int read = decoder.read(buffer);
                if (read == -1) {
                    break;
                }
                decodedByteStream.write(buffer, 0, read);
            }
            decodedByteStream.flush();
            return decodedByteStream.toByteArray();
        } catch (MessagingException me) {
            throw new IOException("Cannot decode data.");
        }
    }
