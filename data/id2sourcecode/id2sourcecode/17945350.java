    private void real_close() throws IOException {
        if (closed) return;
        closed = true;
        in.close();
        verifier.verifyHash(md5.digest(), rcvd);
    }
