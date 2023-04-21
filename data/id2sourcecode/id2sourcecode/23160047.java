    private void setRewriteWithoutRead() throws IOException {
        fileStatus = 43;
        throw new IOException("Rewrite without prior read not allowed on files opened in I-O mode.");
    }
