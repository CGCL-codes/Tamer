    private MessageHeader computeEntry(MessageHeader mh) throws IOException {
        MessageHeader smh = new MessageHeader();
        String name = mh.findValue("Name");
        if (name == null) {
            return null;
        }
        smh.set("Name", name);
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            for (int i = 0; i < hashes.length; ++i) {
                MessageDigest dig = getDigest(hashes[i]);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                mh.print(ps);
                byte[] headerBytes = baos.toByteArray();
                byte[] digest = dig.digest(headerBytes);
                smh.set(hashes[i] + "-Digest", encoder.encode(digest));
            }
            return smh;
        } catch (NoSuchAlgorithmException e) {
            throw new JarException(e.getMessage());
        }
    }
