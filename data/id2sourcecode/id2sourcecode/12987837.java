    public DiskCacheEntry getEntry(String url, Reader is) throws IOException {
        String uri = URIEncoder.encode(url);
        String oldfilename = this.getRoot() + "/old." + uri;
        String filename = DiskCacheUtils.getFile(this, url).getAbsolutePath();
        String newfilename = this.getRoot() + "/new." + uri;
        File file = new File(DiskCacheUtils.getFile(this, url).getAbsolutePath());
        File newfile = new File(newfilename);
        OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(newfile), "utf-8");
        char chars[] = new char[200];
        int readCount = 0;
        while ((readCount = is.read(chars)) > 0) {
            os.write(chars, 0, readCount);
        }
        is.close();
        os.close();
        File oldfile = new File(oldfilename);
        if (oldfile.exists()) oldfile.delete();
        if (newfile.exists() && newfile.length() > 0) {
            file = new File(filename);
            file.renameTo(oldfile);
            newfile.renameTo(file);
        }
        try {
            if (oldfile.exists()) oldfile.delete();
        } catch (Exception e) {
            logger.info("Exception " + e.getMessage() + " while deleting " + oldfilename, e);
        }
        JetspeedDiskCacheEntry dce = (JetspeedDiskCacheEntry) entries.get(url.intern());
        if (dce != null) {
            dce.setFile(file);
            return dce;
        } else {
            return this.getEntry(url, false);
        }
    }
