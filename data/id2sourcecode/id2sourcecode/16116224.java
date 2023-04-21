    private void process(File f, int depth, boolean recursion) {
        if (f.isDirectory()) {
            if (depth == 0 || recursion) {
                process(f.listFiles(), ++depth, recursion);
            }
            return;
        }
        try {
            MessageDigest md5 = getMD5ForFile(f);
            callback.display(f, digestToHexString(md5.digest()));
        } catch (Exception ex) {
            callback.display(f, "[failed]");
        }
    }
