    private static boolean passwordsMatch(String userPassword, String ldapPassword) {
        final String LDAP_PASSWORD_REGEX = "\\{(.+)\\}(.+)";
        Pattern p = Pattern.compile(LDAP_PASSWORD_REGEX);
        Matcher m = p.matcher(ldapPassword);
        boolean match = false;
        if (m.find() && m.groupCount() == 2) {
            String encoding = m.group(1);
            String password = m.group(2);
            if (log.isDebugEnabled()) {
                log.debug("Encoding: " + encoding + ", Password: " + password);
            }
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance(encoding.toUpperCase());
            } catch (NoSuchAlgorithmException e) {
                log.error("Unsupported Algorithm used: " + encoding);
                log.error(e.getMessage());
                return false;
            }
            byte[] resultBytes = digest.digest(userPassword.getBytes());
            byte[] result = Base64.encodeBytesToBytes(resultBytes);
            String pwd = new String(password);
            String ldp = new String(result);
            match = pwd.equals(ldp);
        } else {
            match = userPassword.equals(ldapPassword);
        }
        return match;
    }
