    public String getCRC32() {
        if (fbcnt > 0) update(fbuf, 0, fbcnt);
        fbcnt = 0;
        return "" + crc32.getValue();
    }
