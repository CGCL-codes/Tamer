    private String getHash(Class clazz) throws IOException, NoSuchAlgorithmException {
        String toLoad = "bin/" + clazz.getName().replace(".", "/") + ".class";
        FileInputStream in = new FileInputStream(toLoad);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024 * 32];
        while ((read = in.read(buffer)) != -1) {
            byteOut.write(buffer, 0, read);
        }
        byte[] data = byteOut.toByteArray();
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(data, 0, data.length);
        String hashAsHex = new BigInteger(1, m.digest()).toString(16);
        return hashAsHex;
    }
