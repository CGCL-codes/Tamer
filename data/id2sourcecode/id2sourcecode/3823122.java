    public String generateSharingKey(String studyName) {
        UserService userService = UserServiceFactory.getUserService();
        String key = studyName + userService.getCurrentUser().getUserId();
        try {
            byte messageDigest[] = MessageDigest.getInstance("MD5").digest(key.getBytes());
            StringBuffer hexString = new StringBuffer();
            for (byte element : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & element));
            }
            key = hexString.toString();
            storeSharingKey(studyName, key);
        } catch (Exception ex) {
            return null;
        }
        return key;
    }
