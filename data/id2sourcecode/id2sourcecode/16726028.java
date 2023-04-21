    private void computeHash() throws IOException, NoSuchAlgorithmException {
        hash = 0;
        MessageDigest md = MessageDigest.getInstance("SHA");
        DataOutputStream out = new DataOutputStream(new DigestOutputStream(new ByteArrayOutputStream(127), md));
        out.writeUTF(name);
        out.flush();
        byte[] digest = md.digest();
        for (int i = Math.min(8, digest.length); --i >= 0; ) {
            hash += ((long) (digest[i] & 0xFF)) << (i * 8);
        }
    }
