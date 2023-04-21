    public CommandSet decode(String line) {
        String nline;
        nline = line.trim();
        CommandSet returnSet = null;
        try {
            String substring;
            substring = nline.substring(0, 16);
            if (!substring.equals("<MNDACS-BNP-1.0>")) return null;
            substring = nline.substring(16, nline.indexOf("<sha256>"));
            Base64 base64 = new Base64();
            byte[] buf = base64.decode(substring);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.reset();
            sha.update(buf);
            byte[] checksum = sha.digest();
            substring = nline.substring(nline.indexOf("<sha256>") + 8);
            byte[] decodedChecksum = base64.decode(substring);
            if (!Arrays.equals(checksum, decodedChecksum)) return null;
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf));
            returnSet = (CommandSet) in.readObject();
            in.close();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return returnSet;
    }
