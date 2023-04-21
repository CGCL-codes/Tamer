    public String savePersonalData() throws AlvsanandException {
        User actualUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Globals.SES_USER);
        logger.info("Launched RegistrationBean.savePersonalData[" + actualUser + "]");
        savedPersonalData = true;
        User user = getUserService().getUser(actualUser);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        if (photoData != null && StringUtils.isNotEmpty(photoMediaType)) {
            Avatar avatar = new Avatar();
            avatar.setDate(new Date());
            avatar.setUser(actualUser);
            avatar.setMediaType(photoMediaType);
            avatar.setData(new Blob(photoData));
            user.setAvatar(avatar);
            photoData = null;
            photoMediaType = null;
        }
        if (user != null && user.getPassword() != null) {
            try {
                String passwordDigested = new String(Base64.encodeBase64(getCryptographyService().digest(user.getPassword().getBytes())));
                user.setPassword(passwordDigested);
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
        }
        getUserService().saveUser(user);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Globals.SES_USER, user);
        FacesMessage message = new FacesMessage();
        message.setDetail(MessageResources.getMessage(MessageResources.REGISTRATION_RESOURCE_BUNDLE_NAME, "registration.modifyPersonalData.completed.detail", null));
        message.setSummary(MessageResources.getMessage(MessageResources.REGISTRATION_RESOURCE_BUNDLE_NAME, "registration.modifyPersonalData.completed.summary", null));
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, message);
        password = null;
        rePassword = null;
        showFileUpload = false;
        return MODIFY_PERSONAL_DATA_VIEW_ID;
    }
