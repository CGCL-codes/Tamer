    public void resetPassword(final User user) {
        final String password = PasswordGenerator.generatePassword();
        user.setPassword(this.stringDigester.digest(password));
        this.updateUser(user);
        final Map<String, Object> context = CollectionUtils.getHashMap();
        context.put("user", user);
        context.put("password", password);
        this.notificationService.sendEmail(user.getEmail(), messageSource.getMessage("class.UserServiceImpl.resetPassword.email.subject", null, LocaleContextHolder.getLocale()), context, "get-password");
        LOGGER.info("resetPassword - Email sent to: " + user.getEmail() + "; id: " + user.getId());
    }
