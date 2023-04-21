    public String base64Encode(MessageDigest data) {
        return org.jboss.seam.util.Base64.encodeBytes(data.digest());
    }
