    public static String obfuscateUsername(CnilLevel level, String user) throws Exception {
        MessageDigest anonymizer = getAnonymizer();
        switch(level) {
            case LEVEL_0:
                return StringHelper.nullify(user);
            case LEVEL_1:
                if (StringHelper.nullify(user) == null) return null;
                anonymizer.update(StringHelper.nullify(user).getBytes());
                java.math.BigInteger hash = new java.math.BigInteger(1, anonymizer.digest());
                return hash.toString(16);
            case LEVEL_2:
                return "Anonymized user";
            default:
                _logger.fatal("Unknown CnilLevel (" + level + ")");
                return null;
        }
    }
