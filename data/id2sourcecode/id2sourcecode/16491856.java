    public boolean openConnection(String url, String userPass, char userNamePasswordSeperatorChar) throws SQLException {
        if (url == null) url = RunConfig.getInstance().getConnectionUrlJDBC();
        String user = userPass.substring(0, userPass.indexOf(userNamePasswordSeperatorChar));
        String pass = userPass.substring(userPass.indexOf(userNamePasswordSeperatorChar) + 1);
        if (user == null || user.trim().length() <= 0) user = RunConfig.getInstance().getUserNameJDBC();
        if (pass == null || pass.trim().length() <= 0) pass = RunConfig.getInstance().getPasswordJDBC();
        return openConnection(url, user, pass);
    }
