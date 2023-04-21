    public static byte[] readFully(final InputStream in, final byte[] terminator) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[10];
        boolean noBreak = true;
        for (int readBytes = in.read(buffer); readBytes != -1; readBytes = in.read(buffer)) {
            out.write(buffer, 0, readBytes);
            for (int i = 0; i != terminator.length; i++) {
                if (buffer[readBytes - terminator.length + i - 1] != terminator[i]) {
                    noBreak = true;
                    break;
                }
                noBreak = false;
            }
            if (!noBreak) {
                break;
            }
        }
        return out.toByteArray();
    }
