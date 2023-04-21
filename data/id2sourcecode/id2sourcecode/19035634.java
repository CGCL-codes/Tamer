    private byte[] calculateHash(InputStream jarIS, InputStream itIS, X509Certificate cert) throws CertificateEncodingException, IOException, NoSuchAlgorithmException {
        JarInputStream jis = new JarInputStream(jarIS);
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        ZipEntry ze;
        int readed = 0;
        byte[] buffer = new byte[4096];
        byte[] finalDigest = new byte[HASH_ALGORITHM_LENGTH / 8];
        while ((readed = itIS.read(buffer)) >= 0) md.update(buffer, 0, readed);
        finalDigest = xor(finalDigest, md.digest());
        md.update(cert.getEncoded());
        finalDigest = xor(finalDigest, md.digest());
        while ((ze = jis.getNextEntry()) != null) {
            if ((!ze.isDirectory()) && (!ze.getName().endsWith(".scode"))) {
                md.reset();
                while ((readed = jis.read(buffer)) >= 0) md.update(buffer, 0, readed);
                finalDigest = xor(finalDigest, md.digest());
            }
        }
        return finalDigest;
    }
