    public static String getJarDigest(String path) throws FileNotFoundException, IOException {
        MessageDigest digest = null;
        try {
            digest = getDigest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        if (path != null) {
            JarFile jar = new JarFile(path);
            List<JarEntry> list = Collections.list(jar.entries());
            Collections.sort(list, new JarEntryComparator());
            for (JarEntry entry : list) {
                digest.update(entry.getName().getBytes("ASCII"));
                updateDigest(jar.getInputStream(entry), digest);
            }
        }
        return toHex(digest.digest());
    }
