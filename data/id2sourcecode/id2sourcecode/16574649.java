    public GenerateKeyPair(Properties definition) throws Exception {
        super();
        format = definition.getProperty("format");
        algorithm = definition.getProperty("algorithm");
        bits = Integer.parseInt(definition.getProperty("bits"));
        keyfile = definition.getProperty("keyfile");
        password = definition.getProperty("password");
        subject = definition.getProperty("subject");
        comment = definition.getProperty("comment");
    }
