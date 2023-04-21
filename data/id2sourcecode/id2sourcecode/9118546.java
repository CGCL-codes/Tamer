    public static void extractToFile(Class<?> owner, String resource, String targetPath, boolean force) throws IOException {
        String fileName = StringUtil.getFileName(resource);
        File target;
        if (targetPath == null) target = new File(targetPath, fileName); else target = new File(fileName);
        if (force || !target.isFile()) {
            if (target.isFile()) {
                try {
                    target.delete();
                } catch (SecurityException e) {
                }
            }
            URL swtdllurl = owner.getResource(resource);
            InputStream is;
            is = swtdllurl.openStream();
            OutputStream os = new FileOutputStream(target);
            byte[] buf = new byte[4096];
            int cnt = is.read(buf);
            while (cnt > 0) {
                os.write(buf, 0, cnt);
                cnt = is.read(buf);
            }
            os.close();
            is.close();
        }
    }
