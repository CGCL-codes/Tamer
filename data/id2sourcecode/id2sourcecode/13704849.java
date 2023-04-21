    public static boolean checkFile(File file, String hash) {
        try {
            int size;
            byte[] bytes = new byte[4096];
            FileInputStream input = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            while (input.available() > 0) {
                size = input.read(bytes);
                digest.update(bytes, 0, size);
            }
            byte[] result = digest.digest();
            return hash.equalsIgnoreCase(FileUtil.toString(result));
        } catch (Exception e) {
            return false;
        }
    }
