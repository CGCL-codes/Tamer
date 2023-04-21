    @Override
    public String requestNewPassword(String emailAddress) throws TooOftenPasswordRecoveryException {
        User user = find(emailAddress);
        Calendar currentTime = Calendar.getInstance();
        Calendar lastRequestTime = user.getRecoveryStarted();
        if (lastRequestTime != null) {
            Calendar allowedRequestTime = lastRequestTime;
            allowedRequestTime.add(Calendar.MINUTE, 30);
            if (currentTime.before(allowedRequestTime)) {
                throw new TooOftenPasswordRecoveryException();
            }
        }
        user.setRecoveryStarted(currentTime);
        String digestedConfirmKey = TextUtil.digest(emailAddress + ":" + user.getName() + ":" + TextUtil.getRandomString(10));
        if (digestedConfirmKey == null) {
            throw new RuntimeException();
        }
        user.setPasswordGenerationConfirmKey(digestedConfirmKey);
        edit(user);
        return digestedConfirmKey;
    }
