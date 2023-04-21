    public String sendMail() {
        final List<UserAccountEntity> accounts = m_accountService.findUserAccountsByEmail(m_email);
        if (accounts == null || accounts.isEmpty()) {
            StatusMessages.instance().add("No account found with email address: " + m_email);
            return VIEW_ID;
        }
        m_person = accounts.get(0).getPerson();
        m_accounts = new ArrayList<AccountResetData>();
        final String serverUrl = m_configurationData.getServerUrl();
        for (final UserAccountEntity account : accounts) {
            try {
                final String validation = REQUEST_VALIDATOR.digest(account);
                final String url = serverUrl + "/time4u/reset-password-perform.seam?userId=" + URLEncoder.encode(account.getUserId(), "UTF-8") + "&validation=" + URLEncoder.encode(validation, "UTF-8");
                m_accounts.add(new AccountResetData(account.getUserId(), validation, url));
            } catch (final UnsupportedEncodingException e) {
                LOG.error("Exception", e);
            }
        }
        try {
            m_renderer.render("/mail/reset-password.xhtml");
            StatusMessages.instance().add("An email has been send to you.");
            return PERFORM_VIEW_ID;
        } catch (final Exception e) {
            LOG.error("Exception", e);
            StatusMessages.instance().add("Email sending failed: " + e.getMessage());
        }
        return VIEW_ID;
    }
