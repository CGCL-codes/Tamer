    private String obfuscateUsername(String user) {
        if (cnilLevel == 0) {
            return user;
        } else if (cnilLevel == 1) {
            if (user == null) return null;
            anonymizer.update(user.getBytes());
            java.math.BigInteger hash = new java.math.BigInteger(1, anonymizer.digest());
            return hash.toString(16);
        } else {
            return "Anonymized user";
        }
    }
