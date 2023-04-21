    public String checkSum(String rootPath, String path) {
        File f;
        if (rootPath.endsWith(File.separator)) rootPath = rootPath.substring(0, rootPath.length() - 1);
        rootPath = rootPath + path;
        f = new File(rootPath);
        if (f.isDirectory() || !f.exists() || !f.canRead()) return "0";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(f);
            byte buffer[] = new byte[4096];
            do {
                int read = fis.read(buffer);
                if (read == -1) break;
                if (read != 0) md.update(buffer, 0, read);
            } while (true);
            fis.close();
        } catch (Exception ex) {
            return "0";
        }
        return byteArrayToHexString(md.digest());
    }
