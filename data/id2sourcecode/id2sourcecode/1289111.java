    public void save() throws IOException {
        if (!dirty) return;
        close();
        if (enableSoundCheck) analyzegain();
        Util.copyfile(iPodPath + File.separator + ITUNES_DB, iPodPath + File.separator + ITUNES_DB + ".bak");
        di = new RandomAccessFile(iPodPath + File.separator + ITUNES_DB, "rw");
        di.getChannel().lock();
        di.setLength(0);
        byte[] mhlt;
        byte[] mhsd;
        ByteBuffer mhit = ByteBuffer.allocate(1024 * 1000);
        for (int i = 0, n = db.size(); i < n; i++) {
            FileMeta fm = (FileMeta) db.get(i);
            mhit.put(build_mkhit(fm));
        }
        byte[] mhitb = new byte[mhit.position()];
        mhit.position(0);
        mhit.get(mhitb);
        mhlt = mk_mhlt();
        mhsd = mk_mhsd(mhlt.length + mhitb.length);
        di.write(mk_mhbd(mhlt.length, mhitb.length, mhsd.length));
        di.write(mhsd);
        di.write(mhlt);
        di.write(mhitb);
        close();
        sd.setEnableSoundCheck(enableSoundCheck);
        sd.save(db);
        dirty = false;
    }
