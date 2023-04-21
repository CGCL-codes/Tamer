    @SuppressWarnings("unused")
    private boolean verify() {
        StringBuilder result = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            result.append(TopConstants.APP_SECRET).append("factMoney").append(factMoney).append("gmtCreateDate").append(gmtCreateDate).append("invalidateDate").append(invalidateDate).append("leaseId").append(leaseId).append("nick").append(nick).append("oldVersionNo").append(oldVersionNo).append("status").append(status).append("subscType").append(subscType).append("tadgetCode").append(tadgetCode).append("userId").append(userId).append("validateDate").append(validateDate).append("versionNo").append(versionNo).append(TopConstants.APP_SECRET);
            logger.error("result==" + result.toString());
            byte[] bytes = md5.digest(result.toString().getBytes());
            String encodedString = new String(bytes).toUpperCase();
            logger.error("encodedString==" + encodedString);
            logger.error("sing==" + sign);
            return encodedString.equals(sign);
        } catch (NoSuchAlgorithmException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
