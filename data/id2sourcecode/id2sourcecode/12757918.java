    public void doHashes(MessageHeader mh) throws IOException {
        String name = mh.findValue("Name");
        if (name == null || name.endsWith("/")) {
            return;
        }
        BASE64Encoder enc = new BASE64Encoder();
        for (int j = 0; j < hashes.length; ++j) {
            InputStream is = new FileInputStream(stdToLocal(name));
            try {
                MessageDigest dig = MessageDigest.getInstance(hashes[j]);
                int len;
                while ((len = is.read(tmpbuf, 0, tmpbuf.length)) != -1) {
                    dig.update(tmpbuf, 0, len);
                }
                mh.set(hashes[j] + "-Digest", enc.encode(dig.digest()));
            } catch (NoSuchAlgorithmException e) {
                throw new JarException("Digest algorithm " + hashes[j] + " not available.");
            } finally {
                is.close();
            }
        }
    }
