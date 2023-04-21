    private void handleRegularFile(File file, TarEntry entry) throws FileNotFoundException, IOException {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            InputStream input = null;
            try {
                input = new FileInputStream(file);
                final byte[] buf = new byte[1024];
                int len;
                while ((len = input.read(buf)) > 0) {
                    _tarOut.write(buf, 0, len);
                    md.update(buf, 0, len);
                }
            } finally {
                IOUtils.closeQuietly(input);
            }
            _md5s.put(entry.getName(), new String(Hex.encodeHex(md.digest())));
        } catch (final NoSuchAlgorithmException nsa) {
            throw new RuntimeException("md5 algorthm not found.", nsa);
        }
        _totalSize += bytesToKilobytes(file.length());
    }
