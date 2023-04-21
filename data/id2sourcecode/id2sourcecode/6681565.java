    public String save() throws AlvsanandException {
        logger.info("Launched RegistrationBean.save[" + loginName + "]");
        User user = new User();
        user.setLoginName(loginName);
        user.setPassword(password);
        user.setName(loginName);
        user.setSurname(surname);
        user.setEmail(email);
        Role role = new Role();
        role.setName(GrantedAuthorityImpl.ROLE_GENERIC_USER.getAuthority());
        user.setRole(role);
        user.setState(UserState.REGISTERED.getValue());
        if (user != null && user.getPassword() != null) {
            try {
                String passwordDigested = new String(Base64.encodeBase64(getCryptographyService().digest(user.getPassword().getBytes())));
                user.setPassword(passwordDigested);
                String registrationHash = es.alvsanand.webpage.common.StringUtils.generateRandomString();
                user.setRegistrationHash(registrationHash);
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
        }
        getUserService().saveUser(user);
        getUserService().sendActivationEmail(user);
        FacesMessage message = new FacesMessage();
        message.setDetail(MessageResources.getMessage(MessageResources.REGISTRATION_RESOURCE_BUNDLE_NAME, "registration.end.completed.detail", null));
        message.setSummary(MessageResources.getMessage(MessageResources.REGISTRATION_RESOURCE_BUNDLE_NAME, "registration.end.completed.summary", null));
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return END_REGISTRATION_VIEW_ID;
    }
