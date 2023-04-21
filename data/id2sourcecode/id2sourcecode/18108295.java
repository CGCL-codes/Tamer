    private byte[] createPasswordHash(String password) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA");
            byte[] hashBytes = digest.digest((password).getBytes("UTF-8"));
            return hashBytes;
        } catch (NoSuchAlgorithmException ex) {
            log.error("Cannot create safe password!", ex);
            setPopupMsg("admin.err_pwdhash", ex.getMessage());
            return new byte[0];
        } catch (UnsupportedEncodingException ex) {
            log.error("Cannot convert to UTF-8!", ex);
            setPopupMsg("admin.err_pwdhash", ex.getMessage());
            return new byte[0];
        }
    }
