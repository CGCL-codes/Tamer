    public void init(Session session) {
        this.session = session;
        factory = session.getFactory();
        Properties prop = session.getProperties();
        accountpath = prop.get(ACCOUNTPATH);
        try {
            if (accountpath == null) {
                session.getChannel(PRINT_CHANNEL).PRINT(factory.getConstant("Account Path: "), false, session.getStatus());
                MaverickString s = factory.getString();
                session.getInputChannel().INPUT(s, false, session.getStatus());
                accountpath = s.toString();
                try {
                    File taccount = new File(accountpath);
                    boolean exists = taccount.exists();
                } catch (NullPointerException e) {
                    System.out.println("Caught open exception " + e);
                }
            }
            dbenv = new DbEnv(0);
            dbenv.open(accountpath, Db.DB_INIT_MPOOL, 0);
        } catch (FileNotFoundException fnf) {
            System.err.println("Account not found, creating..." + fnf.toString());
            try {
                dbenv.open(accountpath, Db.DB_INIT_MPOOL | Db.DB_CREATE, 0);
            } catch (DbException dbe2) {
                System.err.println("Database error: " + dbe2.toString());
            } catch (FileNotFoundException fnf2) {
                System.err.println("Could not create account." + fnf2.toString());
            }
        } catch (Exception e) {
            System.out.println("Caught open exception " + e);
        }
    }
