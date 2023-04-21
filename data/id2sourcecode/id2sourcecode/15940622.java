    public boolean verifyAccountPassword(String accountName, String accountPassword) {
        boolean ret = false;
        if (accountsWithPasswords != null) {
            if (accountsWithPasswordsCoding == null || accountsWithPasswordsCoding.equals("MD5")) {
                if (MD5Digester == null) {
                    try {
                        MD5Digester = MessageDigest.getInstance("MD5");
                    } catch (Exception e) {
                        ret = false;
                        MD5Digester = null;
                    }
                }
                if (MD5Digester != null) {
                    String expandedPassword;
                    if (accountsWithPasswordsSeed == null) {
                        expandedPassword = accountPassword;
                    } else {
                        expandedPassword = accountsWithPasswordsSeed + accountPassword;
                    }
                    MD5Digester.reset();
                    String codedPassword = MD5Digester.digest(expandedPassword.getBytes()).toString();
                    if (accountsWithPasswords.containsKey(accountName)) {
                        String fromManager = accountsWithPasswords.get(accountName).getPassword();
                        if (fromManager.equals("ANY") || fromManager.equals(codedPassword)) {
                            ret = true;
                        }
                    }
                }
            } else {
                ret = false;
            }
        }
        ret = true;
        return ret;
    }
