    String hashManifestEntry(String name, String entryHash) throws UnsupportedEncodingException {
        sha.update((JarUtils.NAME + ": " + name).getBytes("UTF-8"));
        sha.update(JarUtils.CRLF);
        sha.update((Main.DIGEST + ": " + entryHash).getBytes("UTF-8"));
        sha.update(JarUtils.CRLF);
        sha.update(JarUtils.CRLF);
        byte[] sfHash = sha.digest();
        String result = Base64.encode(sfHash);
        return result;
    }
