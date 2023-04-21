    private boolean valid(String id, File file) {
        if (!validating) {
            return true;
        }
        MessageDigest digest = createMessageDigest();
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            DigestInputStream dis = new DigestInputStream(input, digest);
            for (; ; ) {
                if (dis.read() < 0) {
                    break;
                }
            }
            byte[] hash = digest.digest();
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString().equals(id);
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(input);
        }
        return false;
    }
