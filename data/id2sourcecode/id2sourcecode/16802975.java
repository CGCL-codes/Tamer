    @Override
    public AddUserService.InsertionResult addUser(String email, String password, String firstName, String lastName) throws IllegalArgumentException {
        try {
            byte[] bytesOfPassword = password.trim().getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesOfPassword);
            pm.currentTransaction().begin();
            DBUser u = new DBUser();
            u.setEmail(email.trim());
            u.setFirstName(firstName.trim());
            u.setLastName(lastName.trim());
            u.setPassword(theDigest);
            pm.makePersistent(u);
            pm.currentTransaction().commit();
        } catch (Throwable t) {
            log.warning("Could not write new user to datastore");
        }
        log.info("Wrote new user to datastore");
        return AddUserService.InsertionResult.OK;
    }
