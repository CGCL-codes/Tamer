    void addManifestEntry(FileInfo fileInfo) {
        String name = fileInfo.getName();
        String md5Text = Utils.toB64(fileInfo.getMd5());
        String sha1Text = Utils.toB64(fileInfo.getSha1());
        String mfest = MessageFormat.format(DIGEST_MANIFST_MESSAGE, name, md5Text, sha1Text);
        manifest.append(mfest).append("\n");
        digestMD5.reset();
        digestSHA.reset();
        try {
            byte[] bytes = mfest.getBytes("LATIN1");
            md5Text = Utils.toB64(digestMD5.digest(bytes));
            sha1Text = Utils.toB64(digestSHA.digest(bytes));
            String ziggy = MessageFormat.format(DIGEST_MANIFST_MESSAGE, name, md5Text, sha1Text);
            zigbert.append(ziggy).append("\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
