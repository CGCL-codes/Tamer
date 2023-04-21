    public void refreshParameterSalt() {
        String uniqueID = null;
        try {
            AndroidID id = AndroidID.newInstance(this);
            uniqueID = id.getAndroidID();
        } catch (Exception e1) {
        }
        if (uniqueID == null) uniqueID = SALTIER_SALT; else uniqueID = uniqueID.concat(SALTIER_SALT);
        try {
            PARAMETER_SALT = uniqueID.getBytes(textEncoding);
        } catch (Exception e) {
            PARAMETER_SALT = uniqueID.getBytes();
        }
        try {
            MessageDigest hasher = MessageDigest.getInstance(SALT_HASH);
            for (int i = 0; i < SALT_ITERATION_COUNT; i++) PARAMETER_SALT = hasher.digest(PARAMETER_SALT);
        } catch (Exception e) {
        }
    }
