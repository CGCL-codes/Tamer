    public static long consume(InputStream input, OutputStream output, boolean closeInput, int bufferSize) throws IOException {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(output);
        long total = 0L;
        int read;
        final byte[] buffer = new byte[max(32, bufferSize)];
        while ((read = input.read(buffer)) > -1) {
            output.write(buffer, 0, read);
            total += read;
        }
        if (closeInput) {
            input.close();
        }
        return total;
    }
