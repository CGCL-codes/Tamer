    public CommandResult run(String[] input) throws SQLException, NoDatabaseConnectionException, InputValidationException {
        if (input.length < 4) {
            System.out.println("Error: insufficient number of arguments provided, syntax of the command is \"" + getSampleInvocation() + "\"");
            return CommandResult.SYNTAX_ERROR;
        } else if (input.length > 5) {
            System.out.println("Error: too many arguments provided, syntax of the command is \"" + getSampleInvocation() + "\"");
            return CommandResult.SYNTAX_ERROR;
        }
        Console console = System.console();
        String password;
        if (console != null) {
            System.out.print("Enter the user's password:");
            password = new String(System.console().readPassword());
            System.out.print("Please the confirm the password:");
            String passwordConfirm = new String(System.console().readPassword());
            if (!password.equals(passwordConfirm)) {
                System.err.println("Error: The passwords do not match, user was not added successfully");
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
        boolean unrestricted = false;
        String hashAlgorithm = application.getApplicationConfiguration().getHashAlgorithm();
        long iterationCount = application.getApplicationConfiguration().getHashIterations();
        EmailAddress emailAddress = null;
        try {
            if (input.length == 5) emailAddress = EmailAddress.getByAddress(input[4]);
        } catch (InvalidLocalPartException e) {
            System.out.println("Error: the local part of the email address is invalid");
            return CommandResult.ERROR;
        } catch (UnknownHostException e) {
            System.out.println("Error: the network part of the email address is invalid");
            return CommandResult.ERROR;
        }
        if (input[3].equalsIgnoreCase("true")) unrestricted = true; else if (input[3].equalsIgnoreCase("false")) unrestricted = false; else {
            System.out.println("Error: the value for the unrestricted argument is invalid, it must be \"true\" or \"false\"");
            return CommandResult.ERROR;
        }
        long newUserId = -1;
        try {
            UserManagement userManagement = new UserManagement(application);
            if (emailAddress != null) newUserId = userManagement.addAccount(input[1], input[2], password, hashAlgorithm, iterationCount, emailAddress, unrestricted); else newUserId = userManagement.addAccount(input[1], input[2], password, hashAlgorithm, iterationCount, null, unrestricted);
            if (newUserId > 0) {
                application.logEvent(EventLogMessage.EventType.USER_ADDED, new EventLogField[] { new EventLogField(EventLogField.FieldName.TARGET_USER_NAME, input[1]), new EventLogField(EventLogField.FieldName.TARGET_USER_ID, newUserId) });
                System.out.println("Successfully added user \"" + input[1] + "\" as user ID = " + newUserId);
            } else {
                application.logEvent(EventLogMessage.EventType.OPERATION_FAILED, new EventLogField[] { new EventLogField(EventLogField.FieldName.OPERATION, "Add new user"), new EventLogField(EventLogField.FieldName.TARGET_USER_NAME, input[1]) });
                System.out.println("Operation failed: user \"" + input[1] + "\" was not added");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Operation failed: user \"" + input[1] + "\" was not added");
        } catch (InputValidationException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
        return CommandResult.EXECUTED_CORRECTLY;
    }
