    @Override
    public void modify(User user, String currentPassword, String newPassword) throws WrongPasswordException {
        String digestedCurrent = TextUtil.digest(currentPassword);
        if (!digestedCurrent.equals(user.getPassword())) {
            throw new WrongPasswordException();
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            String digestedPwd = TextUtil.digest(newPassword);
            userFacade.modify(user, digestedPwd);
        } else {
            userFacade.modify(user);
        }
    }
