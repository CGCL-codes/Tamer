    public ConstantString run(Session session, MaverickString[] args) throws MaverickException {
        this.session = session;
        factory = session.getFactory();
        InputChannel input = session.getInputChannel();
        PrintChannel channel = session.getChannel(Session.SCREEN_CHANNEL);
        MaverickString status = session.getStatus();
        try {
            Driver drv = (Driver) Class.forName(DB_DRIVER).newInstance();
            String user = session.getProperty(PROP_USER, DEFAULT_USER);
            session.PROMPT(ConstantString.EMPTY);
            if (user == null || user.length() == 0) {
                channel.PRINT(factory.getConstant(USER_PROMPT), false, status);
                MaverickString temp = factory.getString();
                input.INPUT(temp, ConstantString.ZERO, true, false, status);
                user = temp.toString();
            }
            String pass = session.getProperty(PROP_PASSWORD, DEFAULT_PASSWORD);
            if (pass == null || pass.length() == 0) {
                channel.PRINT(factory.getConstant(PASS_PROMPT), false, status);
                MaverickString temp = factory.getString();
                input.INPUT(temp, ConstantString.ZERO, true, false, status);
                pass = temp.toString();
            }
            String url = session.getProperty(PROP_URL, DEFAULT_URL);
            conn = DriverManager.getConnection(url, user, pass);
            master = new MasterDictionaryFile(factory);
            UtilResolver resolver = new UtilResolver();
            resolver.run(session, new MaverickString[0]);
            factory.pushResolver(this);
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
            throw new MaverickException(0, cnfe);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace(System.err);
            throw new MaverickException(0, iae);
        } catch (InstantiationException ie) {
            ie.printStackTrace(System.err);
            throw new MaverickException(0, ie);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            throw new MaverickException(0, sqle);
        }
    }
