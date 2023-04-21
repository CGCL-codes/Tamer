    public KeyValuePair decryptUserId(String companyId, String userId, String password) throws PortalException, SystemException {
        Company company = CompanyUtil.findByPrimaryKey(companyId);
        try {
            userId = Encryptor.decrypt(company.getKeyObj(), userId);
        } catch (EncryptorException ee) {
            throw new SystemException(ee);
        }
        String liferayUserId = userId;
        try {
            PrincipalFinder principalFinder = (PrincipalFinder) InstancePool.get(PropsUtil.get(PropsUtil.PRINCIPAL_FINDER));
            liferayUserId = principalFinder.toLiferay(userId);
        } catch (Exception e) {
        }
        User user = UserUtil.findByPrimaryKey(liferayUserId);
        try {
            password = Encryptor.decrypt(company.getKeyObj(), password);
        } catch (EncryptorException ee) {
            throw new SystemException(ee);
        }
        String encPwd = Encryptor.digest(password);
        if (user.getPassword().equals(encPwd)) {
            if (user.isPasswordExpired()) {
                user.setPasswordReset(true);
                UserUtil.update(user);
            }
            return new KeyValuePair(userId, password);
        } else {
            throw new PrincipalException();
        }
    }
