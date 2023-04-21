    private static byte[] getDigest(final File file, final Map<String, byte[]> digests) {
        final String fileName = file.getAbsolutePath();
        if (!digests.containsKey(fileName)) {
            DigestInputStream in = null;
            final MessageDigest digester;
            final byte[] buf = new byte[8192];
            try {
                digester = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException x) {
                throw new RuntimeException("Could not create message digester: " + x, x);
            }
            try {
                in = new DigestInputStream(new FileInputStream(file), digester);
                for (int len; -1 != (in.read(buf, 0, buf.length)); ) ;
                digests.put(fileName, digester.digest());
            } catch (final IOException x) {
                s_log.error("Could not read file: " + x, x);
                return digester.digest();
            } finally {
                if (null != in) try {
                    in.close();
                } catch (final IOException x2) {
                    s_log.error("Could not close file: " + x2, x2);
                }
            }
        }
        return digests.get(fileName);
    }
