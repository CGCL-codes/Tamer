    public String getMessageDigest() {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigestType);
            byte[] badigest = md.digest(data.getBytes());
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < badigest.length; i++) {
                String hex = Integer.toHexString(0xff & badigest[i]);
                if (hex.length() == 1) {
                    buf.append("0");
                }
                buf.append(hex);
            }
            digest = buf.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignerAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (digest);
    }
