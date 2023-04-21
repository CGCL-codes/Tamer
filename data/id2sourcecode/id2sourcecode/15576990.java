    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) return null;
        return CodecUtils.digest(rawPassword.toString());
    }
