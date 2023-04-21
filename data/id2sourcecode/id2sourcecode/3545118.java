    public User addUser(String companyId, boolean autoUserId, String userId, boolean autoPassword, String password1, String password2, boolean passwordReset, String firstName, String middleName, String lastName, String nickName, boolean male, Date birthday, String emailAddress, Locale locale) throws PortalException, SystemException {
        userId = userId.trim().toLowerCase();
        emailAddress = emailAddress.trim().toLowerCase();
        boolean alwaysAutoUserId = GetterUtil.getBoolean(PropsUtil.get(PropsUtil.USERS_ID_ALWAYS_AUTOGENERATE));
        if (alwaysAutoUserId) {
            autoUserId = true;
        }
        validate(companyId, autoUserId, userId, autoPassword, password1, password2, firstName, lastName, emailAddress);
        Company company = CompanyUtil.findByPrimaryKey(companyId);
        if (autoUserId) {
            userId = companyId + "." + Long.toString(CounterManagerUtil.increment(User.class.getName() + "." + companyId));
        }
        User user = UserUtil.create(userId);
        if (autoPassword) {
            password1 = PwdToolkitUtil.generate();
        }
        int passwordsLifespan = GetterUtil.getInteger(PropsUtil.get(PropsUtil.PASSWORDS_LIFESPAN));
        Date expirationDate = null;
        if (passwordsLifespan > 0) {
            expirationDate = new Date(System.currentTimeMillis() + Time.DAY * passwordsLifespan);
        }
        user.setCompanyId(companyId);
        user.setCreateDate(new Date());
        user.setPassword(Encryptor.digest(password1));
        user.setPasswordEncrypted(true);
        user.setPasswordExpirationDate(expirationDate);
        user.setPasswordReset(passwordReset);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setNickName(nickName);
        user.setMale(male);
        user.setBirthday(birthday);
        user.setEmailAddress(emailAddress);
        if (user.hasCompanyMx()) {
            MailManagerUtil.addUser(userId, password1, firstName, middleName, lastName, emailAddress);
        }
        User defaultUser = getDefaultUser(companyId);
        String greeting = null;
        try {
            greeting = LanguageUtil.get(companyId, locale, "welcome") + ", " + user.getFullName() + "!";
        } catch (LanguageException le) {
            greeting = "Welcome, " + user.getFullName() + "!";
        }
        user.setLanguageId(locale.toString());
        user.setTimeZoneId(defaultUser.getTimeZoneId());
        user.setSkinId(defaultUser.getSkinId());
        user.setDottedSkins(defaultUser.isDottedSkins());
        user.setRoundedSkins(defaultUser.isRoundedSkins());
        user.setGreeting(greeting);
        user.setResolution(defaultUser.getResolution());
        user.setRefreshRate(defaultUser.getRefreshRate());
        user.setLayoutIds("");
        user.setActive(true);
        UserUtil.update(user);
        UserConfig userConfig = AdminConfigManagerUtil.getUserConfig(companyId);
        List groups = new ArrayList();
        String groupNames[] = userConfig.getGroupNames();
        for (int i = 0; groupNames != null && i < groupNames.length; i++) {
            try {
                groups.add(GroupUtil.findByC_N(companyId, groupNames[i]));
            } catch (NoSuchGroupException nsge) {
            }
        }
        UserUtil.setGroups(userId, groups);
        List roles = new ArrayList();
        String roleNames[] = userConfig.getRoleNames();
        for (int i = 0; roleNames != null && i < roleNames.length; i++) {
            try {
                Role role = RoleLocalManagerUtil.getRoleByName(companyId, roleNames[i]);
                roles.add(role);
            } catch (NoSuchRoleException nsre) {
            }
        }
        UserUtil.setRoles(userId, roles);
        EmailConfig registrationEmail = userConfig.getRegistrationEmail();
        if (registrationEmail != null && registrationEmail.isSend()) {
            String adminName = company.getAdminName();
            String subject = registrationEmail.getSubject();
            subject = StringUtil.replace(subject, new String[] { "[$ADMIN_EMAIL_ADDRESS$]", "[$ADMIN_NAME$]", "[$COMPANY_MX$]", "[$COMPANY_NAME$]", "[$PORTAL_URL$]", "[$USER_EMAIL_ADDRESS$]", "[$USER_ID$]", "[$USER_NAME$]", "[$USER_PASSWORD$]" }, new String[] { company.getEmailAddress(), adminName, company.getMx(), company.getName(), company.getPortalURL(), user.getEmailAddress(), user.getUserId(), user.getFullName(), password1 });
            String body = registrationEmail.getBody();
            body = StringUtil.replace(body, new String[] { "[$ADMIN_EMAIL_ADDRESS$]", "[$ADMIN_NAME$]", "[$COMPANY_MX$]", "[$COMPANY_NAME$]", "[$PORTAL_URL$]", "[$USER_EMAIL_ADDRESS$]", "[$USER_ID$]", "[$USER_NAME$]", "[$USER_PASSWORD$]" }, new String[] { company.getEmailAddress(), adminName, company.getMx(), company.getName(), company.getPortalURL(), user.getEmailAddress(), user.getUserId(), user.getFullName(), password1 });
            try {
                InternetAddress from = new InternetAddress(company.getEmailAddress(), adminName);
                InternetAddress[] to = new InternetAddress[] { new InternetAddress(user.getEmailAddress(), user.getFullName()) };
                InternetAddress[] cc = null;
                InternetAddress[] bcc = new InternetAddress[] { new InternetAddress(company.getEmailAddress(), adminName) };
                MailManagerUtil.sendEmail(new MailMessage(from, to, cc, bcc, subject, body));
            } catch (IOException ioe) {
                throw new SystemException(ioe);
            }
        }
        return user;
    }
