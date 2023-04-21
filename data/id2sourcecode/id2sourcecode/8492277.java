    public void init() throws IOException {
        FileChannel chan = m_file.getChannel();
        chan.position(m_master.PointerToRawData);
        PEResourceDirectory.ImageResourceDirectory dir = new PEResourceDirectory.ImageResourceDirectory(chan);
        m_root = dir;
    }
