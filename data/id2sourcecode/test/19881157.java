    public String addUser(String name, String role) {
        String fin_name = null;
        if (role != null) name = name + role;
        String command = "sudo /usr/sbin/useradd -K UMASK=000 ";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] msg = name.getBytes();
            md.update(msg);
            byte[] aMessageDigest = md.digest();
            base32 b32 = new base32();
            fin_name = b32.encode(aMessageDigest);
            Process p = runtime.exec(command + fin_name);
            int exitVal = p.waitFor();
            logger.info("the local account " + fin_name + " has been created for the user " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fin_name;
    }
