    public byte[] calculate(Content content, Parse parse) {
        byte[] data = content.getContent();
        if (data == null) data = content.getUrl().getBytes();
        return MD5Hash.digest(data).getDigest();
    }
