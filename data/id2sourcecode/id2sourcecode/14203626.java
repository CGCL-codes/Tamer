    private void login(String user, String password) throws TelnetException {
        boolean pass = true;
        if (readUntil("login:")) write(user); else pass = false;
        if (readUntil("Password:") && pass) write(password);
        if (read("incorrect") || !pass) throw new TelnetException("Authentication Failed");
    }
