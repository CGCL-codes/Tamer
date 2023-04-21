    private String readLine() throws IOException {
        int readBytes = -1;
        byte buffer[] = new byte[10240];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        do {
            readBytes = readLine(buffer, 0, buffer.length);
            if (readBytes != -1) {
                os.write(buffer, 0, readBytes);
            }
        } while (readBytes == buffer.length);
        os.flush();
        byte content[] = os.toByteArray();
        if (content.length == 0) {
            return null;
        } else {
            if (encoding != null && encoding.length() > 0) {
                return new String(content, 0, content.length - 2, encoding);
            } else {
                return new String(content, 0, content.length - 2);
            }
        }
    }
