    public String update() {
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
            String user = identity.getUsername();
            Boolean isAdmin = identity.hasRole("admin");
            if (!user.equals(username.getLogin()) && !isAdmin) {
                facesMessages.add("Only the user or the admin can make updates.");
                return "failure";
            }
            em.merge(username);
            return "success";
        } else {
            facesMessages.add("Passwords are not identical");
            return "failure";
        }
    }
