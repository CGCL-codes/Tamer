    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        StringTokenizer tokens = new StringTokenizer(line);
        if (!authInfo.getUser().getUserName().equals("admin")) {
            tio.write(ColorHelper.colorizeText("This command granted only for user admin\n", ColorHelper.RED));
        } else if (tokens.countTokens() < 2) {
            tio.write("User name must be specified\n");
        } else {
            tokens.nextToken();
            String userName = trimAndRemoveQuotes(tokens.nextToken());
            try {
                UserManager.addUser(new User(userName, null));
                tio.write(MessageFormat.format("User {0} added to user database.\n" + "You must set password for this user for activate.\n", userName));
            } catch (UserAlreadyExist ex) {
                tio.write(MessageFormat.format("User {0} already exist in user database\n", userName));
            }
        }
        tio.flush();
    }
