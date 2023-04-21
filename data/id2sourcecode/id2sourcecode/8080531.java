    private void checkDigest() throws IOException {
        if (!MessageDigest.isEqual(digest, ((DigestInputStream) in).getMessageDigest().digest())) {
            throw new IOException("bad message digest");
        }
    }
