    public String digestAsString(final String str) {
        Assert.notEmpty(str, "str");
        byte[] data = str.getBytes();
        byte[] digest = super.digest(data);
        return new String(digest);
    }
