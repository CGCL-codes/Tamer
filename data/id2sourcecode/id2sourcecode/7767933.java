    public MetaSentence(Session s, String sSentence, SerializerWrite serwrite, SerializerRead serread) {
        super(s);
        m_sSentence = sSentence;
        m_SerWrite = serwrite;
        m_SerRead = serread;
    }
