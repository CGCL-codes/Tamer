    public CommandSet decode(String line) {
        String nline;
        nline = line.trim();
        CommandSet returnSet = null;
        logger.debug("size Message to decode: " + line.length());
        logger.debug(line);
        try {
            String substring;
            if (nline.length() < header.length()) {
                logger.error("string to decode to short");
                return null;
            } else substring = nline.substring(0, header.length());
            if (!substring.equals(header)) return null;
            substring = nline.substring(header.length(), nline.indexOf("<sha256>"));
            Base64 base64 = new Base64();
            byte[] buf = uncompress(base64.decode(substring));
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash_buf = new byte[buf.length + CONFIG.Network_SALT.getBytes().length];
            System.arraycopy(buf, 0, hash_buf, 0, buf.length);
            System.arraycopy(CONFIG.Network_SALT.getBytes(), 0, hash_buf, buf.length, CONFIG.Network_SALT.getBytes().length);
            sha.reset();
            sha.update(hash_buf);
            byte[] checksum = sha.digest();
            substring = nline.substring(nline.indexOf("<sha256>") + 8);
            byte[] decodedChecksum = base64.decode(substring);
            if (!Arrays.equals(checksum, decodedChecksum)) {
                logger.error("checksum failed");
                return null;
            }
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf));
            returnSet = (CommandSet) in.readObject();
            logger.debug(returnSet.getCommandDescription());
            in.close();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return returnSet;
    }
