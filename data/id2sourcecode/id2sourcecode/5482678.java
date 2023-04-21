    public void authenticate(SlideToken slideToken, String login, char[] password) throws AuthenticateException {
        m_reader.authenticate(slideToken, login, password);
        if (m_writer != m_reader) m_writer.authenticate(slideToken, login, password);
    }
