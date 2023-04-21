    public static NSData passwordDigest(String password) {
        _md.reset();
        try {
            _md.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            throw new NSForwardException(uee, "passwordDigest: Password is indigestable.");
        }
        return new NSData(_md.digest());
    }
