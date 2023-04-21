    protected void checkByteCode(String name, byte[] buf) throws GeneralSecurityException {
        Attributes attr;
        Digester dig;
        String[] algs;
        String s;
        Map md;
        int n;
        algs = Digester.parseAlgorithms(Manifest.DEFAULT_TRUSTED);
        dig = new Digester(algs, Manifest.DEFAULT_THRESHOLD);
        attr = (Attributes) static_.get(name);
        if (attr == null) {
            throw new DigestException("No digests available for \"" + name + "\"");
        }
        algs = Digester.parseAlgorithms(attr.getDigestAlgorithms());
        md = new HashMap();
        try {
            dig.digest(algs, md, buf);
        } catch (IOException e) {
            throw new DigestException("Unexpected IOException!");
        }
        for (n = algs.length - 1; n >= 0; n--) {
            if (!md.containsKey(algs[n])) {
                continue;
            }
            s = attr.get(algs[n] + "-Digest");
            if (s == null) {
                throw new DigestException("Missing " + algs[n] + " digest in entry of class \"" + name + "\"");
            }
            buf = (byte[]) md.get(algs[n]);
            try {
                if (!Arrays.equals(buf, Base64.decode(s))) {
                    throw new DigestException(algs[n] + " digest mismatch of class \"" + name + "\"");
                }
            } catch (CorruptedCodeException e) {
                throw new DigestException("Bad Base64 encoding of " + algs[n] + " digest in entry of class \"" + name + "\"");
            }
        }
    }
