    public static void lock() {
        try {
            m_File = new File(".lock");
            m_Channel = new RandomAccessFile(m_File, "rw").getChannel();
            m_Lock = m_Channel.tryLock();
            if (null == m_Lock) System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
