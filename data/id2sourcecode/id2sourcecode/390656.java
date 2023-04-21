    public void create(String[] args, int off) throws IOException {
        if (uid == null || uid.length() == 0) {
            uid = UIDGenerator.getInstance().createUID();
        }
        dirFile.getParentFile().mkdirs();
        DirWriter writer = fact.newDirWriter(dirFile, uid, id, readMeFile, readMeCharset, encodeParam());
        try {
            build(writer, args, off);
        } finally {
            writer.close();
        }
    }
