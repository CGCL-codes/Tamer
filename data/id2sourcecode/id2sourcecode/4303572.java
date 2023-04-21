    public int read(final byte b[], final int off, int len) throws IOException {
        if (!booMark && (bout.toByteArray().length > 0)) {
            byte array[] = bout.toByteArray();
            if (len > array.length) {
                len = array.length;
            }
            System.arraycopy(array, 0, b, off, len);
            bout.reset();
            if (array.length > len) {
                bout.write(array, len, array.length - len);
            }
            return len;
        }
        int read = in.read(b, off, len);
        if (booMark && (read != -1)) {
            bout.write(b, off, read);
        }
        return read;
    }
