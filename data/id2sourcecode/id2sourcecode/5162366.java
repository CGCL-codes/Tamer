    private byte[] getEncodedKeyFrom(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = in.read(buffer)) >= 0) {
            if (read > 0) {
                out.write(buffer, 0, read);
            }
        }
        buffer = out.toByteArray();
        String asText = new String(buffer, "ISO-8859-1");
        int idx = asText.indexOf(BEGIN_PRIVATE_KEY);
        if (idx >= 0) {
            int idxEnd = asText.indexOf(END_PRIVATE_KEY);
            if (idxEnd < 0) {
                throw new IOException("Missing " + END_PRIVATE_KEY);
            }
            StringBuilder nowhitespaces = new StringBuilder();
            for (int i = idx + BEGIN_PRIVATE_KEY.length(); i < idxEnd; i++) {
                char c = asText.charAt(i);
                if (!Character.isWhitespace(c)) {
                    nowhitespaces.append(c);
                }
            }
            buffer = Base64.decode(nowhitespaces.toString());
        }
        return buffer;
    }
