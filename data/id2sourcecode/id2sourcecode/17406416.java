    private String obfuscateUsername(String user) {
        if (user == null) return null;
        if (cnilLevel == 0) {
            return user;
        } else if (cnilLevel == 1) {
            anonymizer.update(user.getBytes());
            java.math.BigInteger hash = new java.math.BigInteger(1, anonymizer.digest());
            return hash.toString(16);
        } else {
            return "Anonymized user";
        }
    }
