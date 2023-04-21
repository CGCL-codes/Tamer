    public void send() throws Exception {
        initTemplate();
        Smtp smtp = new Smtp();
        smtp.connect(m_config.getString("smtp.server"), Integer.parseInt(m_config.getString("smtp.port")), Boolean.parseBoolean(m_config.getString("smtp.ssl")), Boolean.parseBoolean(m_config.getString("smtp.auth")));
        if (m_config.getString("smtp.username") != null) {
            smtp.login(m_config.getString("smtp.username"), m_config.getString("smtp.password"));
        }
        smtp.send(m_message);
    }
