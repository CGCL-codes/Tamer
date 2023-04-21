    String hashByteArray(byte[] ba) throws IOException {
        sha.update(ba);
        byte[] hash = sha.digest();
        if (Configuration.DEBUG) log.finest("Hashed " + ba.length + " byte(s)");
        String result = Base64.encode(hash);
        return result;
    }
