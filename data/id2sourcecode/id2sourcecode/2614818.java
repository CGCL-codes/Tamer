    private String getUniqueFilename(MimeMessage msg) throws IOException, MessagingException, NoSuchAlgorithmException {
        byte[] bytes = msg.getContent().toString().getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] digest = sha.digest(bytes);
        return exportDir + System.currentTimeMillis() + hexEncode(digest);
    }
