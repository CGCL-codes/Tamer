    protected void activate(ComponentContext context) {
        if (this.logService != null) {
            this.logService.log(LogService.LOG_INFO, "Activating Temperature Sensor Evaluator");
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] resultingBytes = md5.digest(this.uuId.getBytes());
            this.merId = Base64.encodeBase64URLSafeString(resultingBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
