    private String readString(DataInputStream in) throws IOException {
        int len = in.readShort();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len > 0) {
            int read = (len > buffer.length) ? buffer.length : len;
            in.read(buffer, 0, read);
            out.write(buffer, 0, read);
            len -= read;
        }
        return out.toString();
    }
