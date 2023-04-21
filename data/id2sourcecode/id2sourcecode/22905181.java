    public CommandResult run(String input[]) {
        if (input.length < 2) {
            System.out.println("Error: too few arguments provided, syntax of the command is \"" + getSampleInvocation() + "\"");
            return CommandResult.SYNTAX_ERROR;
        }
        if (input.length > 2) {
            System.out.println("Error: too many arguments provided, syntax of the command is \"" + getSampleInvocation() + "\"");
            return CommandResult.SYNTAX_ERROR;
        }
        UserManagement userManagement = new UserManagement(application);
        int userId;
        try {
            userId = userManagement.getUserID(input[1]);
        } catch (SQLException e) {
            application.logExceptionEvent(EventLogMessage.EventType.SQL_EXCEPTION, e);
            System.out.println("Password could not be set, a SQL exception occurred");
            return CommandResult.ERROR;
        } catch (NoDatabaseConnectionException e) {
            application.logExceptionEvent(EventLogMessage.EventType.DATABASE_FAILURE, e);
            System.out.println("Password could not be set, no database connection exists");
            return CommandResult.ERROR;
        } catch (InputValidationException e) {
            System.out.println("Username is illegal (contains disallowed characters)");
            application.logEvent(EventLogMessage.EventType.USER_NAME_ILLEGAL, new EventLogField(FieldName.TARGET_USER_NAME, input[1]));
            return CommandResult.ERROR;
        }
        if (userId < 0) {
            System.out.println("No user exists with the name given");
            application.logEvent(EventLogMessage.EventType.USER_ID_INVALID, new EventLogField(FieldName.TARGET_USER_NAME, input[1]), new EventLogField(FieldName.TARGET_USER_ID, userId));
            return CommandResult.ERROR;
        }
        String password;
        Console console = System.console();
        if (console != null) {
            System.out.print("Enter the user's password:");
            password = new String(System.console().readPassword());
            System.out.print("Please the confirm the password:");
            String passwordConfirm = new String(System.console().readPassword());
            if (!password.equals(passwordConfirm)) {
                System.err.println("Error: The passwords do not match, user's password was not updated");
                return CommandResult.ERROR;
            }
        } else {
            InputStream inputStreamChannel = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader in = null;
            try {
                inputStreamChannel = Channels.newInputStream((new FileInputStream(FileDescriptor.in)).getChannel());
                inputStreamReader = new InputStreamReader(inputStreamChannel);
                in = new BufferedReader(inputStreamReader);
                System.out.print("Enter the user's password: ");
                password = in.readLine();
            } catch (IOException e) {
                System.err.println("Password was not successfully read");
                return CommandResult.ERROR;
            }
        }
        try {
            if (userManagement.changePassword(userId, password)) System.out.println("Password successfully changed"); else System.out.println("Password was not successfully changed");
        } catch (SQLException e) {
            System.out.println("Password could not be set, a SQL exception occurred");
            application.logExceptionEvent(EventLogMessage.EventType.SQL_EXCEPTION, e);
            return CommandResult.ERROR;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Password could not be set, the hash algorithm is unknown");
            application.logExceptionEvent(EventLogMessage.EventType.INTERNAL_ERROR, e);
            return CommandResult.ERROR;
        } catch (NoDatabaseConnectionException e) {
            System.out.println("Password could not be set, no database connection exists");
            application.logExceptionEvent(EventLogMessage.EventType.DATABASE_FAILURE, e);
            return CommandResult.ERROR;
        } catch (InputValidationException e) {
            System.out.println("Password is illegal (contains disallowed characters)");
            application.logEvent(EventLogMessage.EventType.PASSWORD_ILLEGAL, new EventLogField(FieldName.TARGET_USER_ID, userId));
            return CommandResult.ERROR;
        }
        return CommandResult.EXECUTED_CORRECTLY;
    }
