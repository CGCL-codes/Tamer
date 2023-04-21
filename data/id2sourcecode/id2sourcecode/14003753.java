    public String register() {
        if (password.equals(confirm)) {
            String hash = null;
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
                byte raw[] = md.digest(password.getBytes());
                hash = encodeBase16(raw);
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("Register.register() action called");
            Username username = usernameHome.getInstance();
            username.setPasswd(hash);
            em.persist(username);
            return "success";
        } else {
            facesMessages.add("Passwords are not identical.");
            return "failure";
        }
    }
