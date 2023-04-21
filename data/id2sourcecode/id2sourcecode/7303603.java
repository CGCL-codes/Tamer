    public ConstantString run(Session session, MaverickString[] args) throws MaverickException {
        this.session = session;
        factory = session.getFactory();
        catalog = session.getProperty(PROP_CATALOG, DEFAULT_CATALOG);
        schema = session.getProperty(PROP_SCHEMA, DEFAULT_SCHEMA);
        String masterdict = session.getProperty(PROP_MASTER_DICT, DEFAULT_MASTER_DICT);
        try {
            Class mdclass = Class.forName(masterdict);
            Class[] parameters = { Factory.class };
            Constructor constructor = mdclass.getConstructor(parameters);
            Object[] parameters2 = { factory };
            master = (MasterDictionaryFile) constructor.newInstance(parameters2);
        } catch (ClassNotFoundException cnfe2) {
            throw new MaverickException(0, cnfe2);
        } catch (IllegalAccessException iae2) {
            throw new MaverickException(0, iae2);
        } catch (InstantiationException ie) {
            throw new MaverickException(0, ie);
        } catch (InvocationTargetException ite) {
            throw new MaverickException(0, ite);
        } catch (NoSuchMethodException nsme) {
            throw new MaverickException(0, nsme);
        }
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
            UtilResolver resolver = new UtilResolver();
            resolver.run(session, new MaverickString[0]);
            factory.pushResolver(this);
            return null;
        } catch (ClassNotFoundException cnfe) {
            throw new MaverickException(0, cnfe);
        } catch (IllegalAccessException iae) {
            throw new MaverickException(0, iae);
        } catch (InstantiationException ie) {
            throw new MaverickException(0, ie);
        } catch (SQLException sqle) {
            throw new MaverickException(0, sqle);
        }
    }
