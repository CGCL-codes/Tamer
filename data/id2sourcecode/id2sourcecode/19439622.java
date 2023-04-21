    public String encode(CommandSet cmd) {
        String returnString = "";
        try {
            returnString = "<MNDACS-BNP-1.0>";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(cmd);
            out.close();
            byte[] buf = bos.toByteArray();
            Base64 base64 = new Base64();
            returnString += base64.encodeToString(buf);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.reset();
            sha.update(buf);
            byte[] checksum = sha.digest();
            returnString += "<sha256>";
            returnString += base64.encodeToString(checksum);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return returnString;
    }
