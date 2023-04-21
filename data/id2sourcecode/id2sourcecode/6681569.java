    public String resetPassword() throws AlvsanandException {
        logger.info("Launched RegistrationBean.resetPassword[" + loginName + "]");
        User user = getUserService().getUserByLoginName(loginName);
        if (user == null) {
            FacesMessage message = new FacesMessage();
            message.setDetail(MessageResources.getMessage(MessageResources.ERROR_RESOURCE_BUNDLE_NAME, "error.resetPassword.loginName.detail", null));
            message.setSummary(MessageResources.getMessage(MessageResources.ERROR_RESOURCE_BUNDLE_NAME, "error.resetPassword.loginName.summary", null));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return END_RESET_PASSWORD_VIEW_ID;
        }
        String plainPassword = es.alvsanand.webpage.common.StringUtils.generateRandomString();
        try {
            String passwordDigested = new String(Base64.encodeBase64(getCryptographyService().digest(plainPassword.getBytes())));
            user.setPassword(passwordDigested);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        getUserService().saveUser(user);
        getUserService().sendResetPasswordEmail(user, plainPassword);
        FacesMessage message = new FacesMessage();
        message.setDetail(MessageResources.getMessage(MessageResources.REGISTRATION_RESOURCE_BUNDLE_NAME, "registration.resetPassword.activation.completed.detail", null));
        message.setSummary(MessageResources.getMessage(MessageResources.REGISTRATION_RESOURCE_BUNDLE_NAME, "registration.resetPassword.activation.completed.summary", null));
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return END_RESET_PASSWORD_VIEW_ID;
    }
