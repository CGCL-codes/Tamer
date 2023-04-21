    void startManifestEntries() {
        digestMD5.reset();
        digestSHA.reset();
        byte[] bytes = null;
        try {
            bytes = MANIFEST_PREAMBLE.getBytes("LATIN1");
        } catch (UnsupportedEncodingException e) {
        }
        byte[] md5 = digestMD5.digest(bytes);
        byte[] sha = digestSHA.digest(bytes);
        String md5Text = Utils.toB64(md5);
        String shaText = Utils.toB64(sha);
        String zigbertR = MessageFormat.format(DIGEST_MESSAGE, md5Text, shaText);
        manifest.append(MANIFEST_PREAMBLE).append("\n");
        zigbert.append(ZIGBERT_PREAMBLE).append(zigbertR).append("\n");
    }
