    private void sendAuthMethod() throws SpreadException {
        int len = authName.length();
        byte buffer[] = new byte[MAX_AUTH_NAME * MAX_AUTH_METHODS];
        try {
            System.arraycopy(authName.getBytes("ISO8859_1"), 0, buffer, 0, len);
        } catch (UnsupportedEncodingException e) {
        }
        for (; len < (MAX_AUTH_NAME * MAX_AUTH_METHODS); len++) buffer[len] = 0;
        try {
            socketOutput.write(buffer);
        } catch (IOException e) {
            throw new SpreadException("write(): " + e);
        }
    }
