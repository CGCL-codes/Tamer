    public static void processFile(File file) throws Exception {
        if (file.isDirectory()) {
            processFiles(file.listFiles());
        } else {
            MessageDigest md5 = getMD5ForFile(file);
            displayResult(file, md5.digest());
        }
    }
