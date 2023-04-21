    private void fillAccountSet(Map<String, String> accounts, String section, boolean isEncrypted) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        for (String name : ini.optionNames(section)) {
            String password = ini.get(section, name);
            if (!isEncrypted) {
                password = new String(md.digest(password.getBytes()));
            }
            accounts.put(name, password);
        }
    }
