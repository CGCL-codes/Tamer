    private void saveLastKnownGood(FileChannel c) throws IOException {
        FileOutputStream lkg = new FileOutputStream(getPath() + ".lkg", false);
        FileChannel lkgChan = lkg.getChannel();
        try {
            lkgChan.truncate(c.size());
            c.transferTo(0, c.size(), lkgChan);
        } finally {
            lkgChan.close();
        }
    }
