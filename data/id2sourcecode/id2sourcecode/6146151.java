    @Test
    public void testLoginByUsername() throws Exception {
        String loginName = "loginName";
        String password = new String(Base64.encodeBase64(cryptographyService.digest("password".getBytes())));
        User user = new User();
        user.setLoginName(loginName);
        user.setPassword(password);
        user.setState(UserState.ACCEPTED.ordinal());
        userAdminService.saveOrUpdateUser(user);
        loginService.loginByUsername(loginName, password);
    }
