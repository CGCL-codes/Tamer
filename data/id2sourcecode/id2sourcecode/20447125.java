    public Session getSession() throws DataAdapterException {
        try {
            System.out.println("path = " + path);
            URL url = new URL(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String refIOR = in.readLine();
            System.out.println("Got IOR = " + refIOR);
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(new String[0], new java.util.Properties());
            System.out.println("Got orb = " + orb);
            org.omg.CORBA.Object manObj = orb.string_to_object(refIOR);
            System.out.println("Got manObj = " + manObj);
            SessionManager mySessMan = SessionManagerHelper.narrow(manObj);
            System.out.println("Got sessman = " + mySessMan);
            Param[] nullParams = new Param[0];
            mySess = mySessMan.initiate_Session(nullParams);
            System.out.println("Got sess = " + mySess);
            mySess.connect(sessParams);
            System.out.println("connected = " + mySess);
            return mySess;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAdapterException("Error creating CORBA session: " + e.toString(), e);
        }
    }
