    private void verifyDigests() throws IOException {
        for (int i = 0; i < digest.length; i++) {
            byte rc[] = digest[i].digest();
            if (!MessageDigest.isEqual(result[i], rc)) throw new IOException("Corrupted file: the digest is valid for " + digest[i].getAlgorithm());
        }
    }
