    private static byte[] createSHA1(final File file, boolean digest) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        MessageDigest md = null;
        if (digest) md = MessageDigest.getInstance("SHA"); else md = new SHA1();
        try {
            byte[] buffer = new byte[16384];
            int read;
            while ((read = fis.read(buffer)) != -1) md.update(buffer, 0, read);
        } finally {
            fis.close();
        }
        return md.digest();
    }
