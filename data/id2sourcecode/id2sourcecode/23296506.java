    private void copy(InputStream in, OutputStream out) throws IOException {
        int read;
        while ((read = in.read(buffer)) != -1) {
            totalSize += read;
            if (out != null) {
                out.write(buffer, 0, read);
            }
        }
    }
