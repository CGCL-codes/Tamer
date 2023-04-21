    private void digest() throws IOException {
        byte[] digest = md.digest();
        out.write(digest, 0, digest.length);
        md.reset();
    }
