    public static String generateKey(String scopeId, boolean local) {
        if (scopeId == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] id = scopeId.getBytes();
            md.update(id);
            if (local) {
                byte[] now = new Long(System.currentTimeMillis()).toString().getBytes();
                md.update(now);
            }
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
    }
