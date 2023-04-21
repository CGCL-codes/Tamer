    void getNameService() {
        org.omg.CORBA.Object obj = null;
        String iorURL = the_props.getProperty("NameService", "");
        if (iorURL != "") {
            try {
                URL url = new URL(iorURL);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String ref = in.readLine();
                in.close();
                obj = the_orb.string_to_object(ref);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            try {
                obj = the_orb.resolve_initial_references("NameService");
            } catch (org.omg.CORBA.ORBPackage.InvalidName ex) {
                System.out.println("Can't resolve `NameService'");
                System.exit(1);
            }
        }
        if (obj == null) {
            System.err.println("'NameService' is a nil object reference");
            System.exit(1);
        }
        try {
            nc = org.omg.CosNaming.NamingContextHelper.narrow(obj);
            if (nc == null) {
                System.err.println("'NameService' is not " + "a NamingContext object reference");
                System.exit(1);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }
        result.append("Found NameService\n");
    }
