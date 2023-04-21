    public void read() throws IOException {
        FileChannel ch = m_pe.getChannel();
        ByteBuffer mz = ByteBuffer.allocate(64);
        mz.order(ByteOrder.LITTLE_ENDIAN);
        ch.read(mz, 0);
        mz.position(0);
        byte m = mz.get();
        byte z = mz.get();
        if ((m == 77) && (z == 90)) {
        }
        e_cblp = mz.getShort();
        e_cp = mz.getShort();
        e_crlc = mz.getShort();
        e_cparhdr = mz.getShort();
        e_minalloc = mz.getShort();
        e_maxalloc = mz.getShort();
        e_ss = mz.getShort();
        e_sp = mz.getShort();
        e_csum = mz.getShort();
        e_ip = mz.getShort();
        e_cs = mz.getShort();
        e_lfarlc = mz.getShort();
        e_ovno = mz.getShort();
        for (int i = 0; i < 4; i++) e_res[i] = mz.getShort();
        e_oemid = mz.getShort();
        e_oeminfo = mz.getShort();
        for (int i = 0; i < 10; i++) e_res2[i] = mz.getShort();
        e_lfanew = mz.getInt();
    }
