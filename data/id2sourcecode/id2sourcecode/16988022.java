    public void save(ArrayList db) throws IOException {
        close();
        Util.copyfile(iPodPath + File.separator + ITUNES_SD, iPodPath + File.separator + ITUNES_SD + ".bak");
        di = new RandomAccessFile(iPodPath + File.separator + iTunesSD.ITUNES_SD, "rw");
        di.getChannel().lock();
        di.setLength(0);
        ByteBuffer buf = ByteBuffer.allocate(1024 * 240);
        buf.put(mk_itunes_sd_header(db.size()));
        for (int i = 0, n = db.size(); i < n; i++) {
            FileMeta fm = (FileMeta) db.get(i);
            buf.put(mk_itunes_sd_file(fm));
        }
        byte[] bufb = new byte[buf.position()];
        buf.position(0);
        buf.get(bufb);
        di.write(bufb);
        new File(iPodPath + File.separator + ITUNES_SHUFFLE).delete();
        new File(iPodPath + File.separator + ITUNES_STATS).delete();
        close();
    }
