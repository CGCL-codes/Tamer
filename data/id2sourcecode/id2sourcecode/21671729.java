    public static boolean fileCopy(InputStream sourceFile, File destFile) {
        try {
            InputStream in = sourceFile;
            FileOutputStream out = new FileOutputStream(destFile);
            byte buf[] = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            in.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
