    protected String md5(String input) {
        this.md5Algorithm.update(input.getBytes());
        StringBuffer md5 = new StringBuffer();
        byte[] digestedBytes = this.md5Algorithm.digest();
        for (byte digestedByte : digestedBytes) {
            String hex = Integer.toHexString(0xFF & digestedByte);
            hex = hex.length() < 2 ? "0" + hex : hex;
            md5.append(hex);
        }
        return md5.toString();
    }
