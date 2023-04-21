    private String generateUUID() throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        if ((this.getWSDLContent() == null) || (this.getWSDLContent().length() == 0)) {
            if ((this.getEndpoint() != null) && (this.getEndpoint().length() != 0)) {
                this.extractWSDLContent(this.getEndpoint());
            } else {
                throw new IOException("WSDL Content could not be loaded");
            }
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = md5.digest(this.getWSDLContent().getBytes("UTF-8"));
        return hexEncode(result);
    }
