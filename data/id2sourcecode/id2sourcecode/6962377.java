    public String digest(String identifier, String key) {
        if (identifier != null) md.update(identifier.getBytes());
        if (key != null) md.update(key.getBytes());
        return DESSHA1.bytesToHex(md.digest());
    }
