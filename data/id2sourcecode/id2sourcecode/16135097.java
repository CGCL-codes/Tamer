    @Override
    public UserBean doLogin(UserLoginDTO dto) {
        User user = userDao.queryUserByEmail(dto.getLoginId());
        UserBean userBean = null;
        if (user != null) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                Base64 b64 = new Base64();
                String password = new String(b64.encode(digest.digest(user.getPassword().getBytes())));
                if (password.equals(dto.getPassword())) {
                    userBean = new UserBean(user.getId(), user.getName());
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return userBean;
    }
