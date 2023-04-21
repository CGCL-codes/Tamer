    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            if (out != null) {
                out.write(buffer, 0, read);
            }
        }
        in.close();
        out.close();
    }
