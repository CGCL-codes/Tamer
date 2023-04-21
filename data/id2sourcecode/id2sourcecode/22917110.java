    public void setPassword(String password) {
        if (!passwordEnable) {
            log.warn("passwordEnable should be true if you intend to set password attribute.");
            return;
        }
        this.password = password;
        try {
            this.replace(0, this.getLength(), Util.fillString(this.getPasswordEchoChar(), password != null ? password.length() : 0), null);
        } catch (BadLocationException e) {
            log.debug(e.getMessage());
        }
        if (this.getDigestAlgorithm() != null && this.getDigestAlgorithm().length() > 0) {
            this.digestPassword = password == null || password.length() == 0 ? password : Digest.digest(this.getPassword(), getDigestAlgorithm());
        }
    }
