    public String digout() {
        byte[] digest = this.digest.digest();
        if (digest != null) return StringUtils.hexEncode(digest); else return null;
    }
