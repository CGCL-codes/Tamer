    public static void copy(File src, File dst) throws IOException {
        if (src.isDirectory() && dst.isDirectory()) {
            org.apache.commons.io.FileUtils.copyDirectory(src, dst);
        } else if (src.isFile()) {
            if (dst.isFile()) {
                org.apache.commons.io.FileUtils.copyFile(src, dst);
            } else if (dst.isDirectory()) {
                org.apache.commons.io.FileUtils.copyFileToDirectory(src, dst);
            }
        }
    }
