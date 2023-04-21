    public String resetPassword() {
        final UserAccountEntity account = m_accountService.getUserAccount(m_userId);
        if (account == null) {
            StatusMessages.instance().add("Account " + m_userId + " not found");
            return PERFORM_VIEW_ID;
        }
        final String validation = REQUEST_VALIDATOR.digest(account);
        if (!validation.equals(m_validation)) {
            StatusMessages.instance().add("Validation string invalid");
            return PERFORM_VIEW_ID;
        }
        if (!m_newPassword.equals(m_newPasswordConfirm)) {
            StatusMessages.instance().add("Password and password confirmation do not match");
            return PERFORM_VIEW_ID;
        }
        account.setHashedPassword(new DefaultPasswordEncoder().encrypt(m_newPassword.toCharArray()));
        return "/login.xhtml";
    }
