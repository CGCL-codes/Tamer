    public Digest(File appdir) throws IOException {
        StringBuilder data = new StringBuilder();
        File dfile = new File(appdir, DIGEST_FILE);
        for (String[] pair : ConfigUtil.parsePairs(dfile, false)) {
            if (pair[0].equals(DIGEST_FILE)) {
                _metaDigest = pair[1];
                break;
            }
            _digests.put(pair[0], pair[1]);
            note(data, pair[0], pair[1]);
        }
        MessageDigest md = getMessageDigest();
        byte[] contents = data.toString().getBytes("UTF-8");
        String md5 = StringUtil.hexlate(md.digest(contents));
        if (!md5.equals(_metaDigest)) {
            String err = MessageUtil.tcompose("m.invalid_digest_file", _metaDigest, md5);
            throw new IOException(err);
        }
    }
