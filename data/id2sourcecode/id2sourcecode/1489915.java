    public void setData(byte[] data) {
        this.data = data;
        try {
            byte[] digest;
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(data);
            this.hash = "";
            for (int i = 0; i < digest.length; i++) {
                this.hash += Integer.toHexString((int) digest[i] & 0xff);
            }
        } catch (Exception e) {
            this.hash = null;
        }
    }
