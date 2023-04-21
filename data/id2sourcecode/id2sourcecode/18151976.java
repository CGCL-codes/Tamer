    public String encode(CommandSet cmd) {
        String returnString = "";
        try {
            returnString = header;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(cmd);
            out.close();
            byte[] buf = bos.toByteArray();
            Base64 base64 = new Base64();
            returnString += base64.encodeToString(compress(buf));
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash_buf = new byte[buf.length + CONFIG.Network_SALT.getBytes().length];
            System.arraycopy(buf, 0, hash_buf, 0, buf.length);
            System.arraycopy(CONFIG.Network_SALT.getBytes(), 0, hash_buf, buf.length, CONFIG.Network_SALT.getBytes().length);
            sha.reset();
            sha.update(hash_buf);
            byte[] checksum = sha.digest();
            returnString += "<sha256>";
            returnString += base64.encodeToString(checksum);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        logger.debug("size encoded Message: " + returnString.length());
        return returnString;
    }
