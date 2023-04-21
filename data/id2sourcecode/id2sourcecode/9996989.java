    private String readStreamContent(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int read = in.read(buffer);
        if (read == -1) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        do {
            out.write(buffer, 0, read);
        } while ((read = in.read(buffer)) >= 0);
        return out.toString();
    }
