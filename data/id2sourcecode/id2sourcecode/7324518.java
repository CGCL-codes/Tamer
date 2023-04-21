    public static UID generateUniqueId(byte[] content) throws UIDException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if (content != null) {
                ByteArrayInputStream in = new ByteArrayInputStream(content);
                DigestInputStream dis = new DigestInputStream(in, messageDigest);
                while (dis.read() != -1) {
                    ;
                }
                dis.close();
                in.close();
            }
            byte[] noise = new byte[1024];
            ConfigurationLoader.getRND().nextBytes(noise);
            messageDigest.update(noise);
            UID uid = new UID(messageDigest.digest());
            return uid;
        } catch (Exception ex) {
            throw new UIDException("Failed to generate a unique identifier: " + ex.getMessage());
        }
    }
