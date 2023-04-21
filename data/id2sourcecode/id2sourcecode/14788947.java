    public static byte[] hashIcon(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        try {
            byte[] block = new byte[1024];
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
            for (; ; ) {
                int count = in.read(block);
                if (count == -1) break;
                md.update(block, 0, count);
            }
            return md.digest();
        } finally {
            in.close();
        }
    }
