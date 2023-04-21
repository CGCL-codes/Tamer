    private int _authenticate(String companyId, String login, String password, boolean byEmailAddress) throws PortalException, SystemException {
        login = login.trim().toLowerCase();
        if (byEmailAddress) {
            if (!Validator.isEmailAddress(login)) {
                throw new UserEmailAddressException();
            }
        } else {
            if (Validator.isNull(login)) {
                throw new UserIdException();
            }
        }
        if (Validator.isNull(password)) {
            throw new UserPasswordException(UserPasswordException.PASSWORD_INVALID);
        }
        int authResult = Authenticator.FAILURE;
        if (byEmailAddress) {
            authResult = AuthPipeline.authenticateByEmailAddress(PropsUtil.getArray(PropsUtil.AUTH_PIPELINE_PRE), companyId, login, password);
        } else {
            authResult = AuthPipeline.authenticateByUserId(PropsUtil.getArray(PropsUtil.AUTH_PIPELINE_PRE), companyId, login, password);
        }
        User user = null;
        try {
            if (byEmailAddress) {
                user = UserUtil.findByC_EA(companyId, login);
            } else {
                user = UserUtil.findByC_U(companyId, login);
            }
        } catch (NoSuchUserException nsue) {
            return Authenticator.DNE;
        }
        if (!user.isPasswordEncrypted()) {
            user.setPassword(Encryptor.digest(user.getPassword()));
            user.setPasswordEncrypted(true);
            user.setPasswordReset(GetterUtil.getBoolean(PropsUtil.get(PropsUtil.PASSWORDS_CHANGE_ON_FIRST_USE)));
            UserUtil.update(user);
        } else if (user.isPasswordExpired()) {
            user.setPasswordReset(true);
            UserUtil.update(user);
        }
        if (authResult == Authenticator.SUCCESS) {
            String encPwd = Encryptor.digest(password);
            if (user.getPassword().equals(encPwd)) {
                authResult = Authenticator.SUCCESS;
            } else {
                authResult = Authenticator.FAILURE;
            }
        }
        if (authResult == Authenticator.SUCCESS) {
            if (byEmailAddress) {
                authResult = AuthPipeline.authenticateByEmailAddress(PropsUtil.getArray(PropsUtil.AUTH_PIPELINE_POST), companyId, login, password);
            } else {
                authResult = AuthPipeline.authenticateByUserId(PropsUtil.getArray(PropsUtil.AUTH_PIPELINE_POST), companyId, login, password);
            }
        }
        if (authResult == Authenticator.FAILURE) {
            try {
                if (byEmailAddress) {
                    AuthPipeline.onFailureByEmailAddress(PropsUtil.getArray(PropsUtil.AUTH_FAILURE), companyId, login);
                } else {
                    AuthPipeline.onFailureByUserId(PropsUtil.getArray(PropsUtil.AUTH_FAILURE), companyId, login);
                }
                int failedLoginAttempts = user.getFailedLoginAttempts();
                user.setFailedLoginAttempts(++failedLoginAttempts);
                UserUtil.update(user);
                int maxFailures = GetterUtil.get(PropsUtil.get(PropsUtil.AUTH_MAX_FAILURES_LIMIT), 0);
                if ((failedLoginAttempts >= maxFailures) && (maxFailures != 0)) {
                    if (byEmailAddress) {
                        AuthPipeline.onMaxFailuresByEmailAddress(PropsUtil.getArray(PropsUtil.AUTH_MAX_FAILURES), companyId, login);
                    } else {
                        AuthPipeline.onMaxFailuresByUserId(PropsUtil.getArray(PropsUtil.AUTH_MAX_FAILURES), companyId, login);
                    }
                }
            } catch (Exception e) {
                Logger.error(this, e.getMessage(), e);
            }
        }
        return authResult;
    }
