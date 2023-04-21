    public void logout() {
        m_reader.logout();
        if (m_writer != m_reader) m_writer.logout();
    }
