    public static void main(String args[]) {
        SessionManager mySessMan = null;
        org.omg.CORBA.ORB orb = null;
        String urlStr = "";
        String extId = "";
        Param[] sessParams;
        boolean doAll = false;
        int argNum = 0;
        if (args.length < 2) {
            System.out.println("Usage: TestApolloClient [-a] <IOR url> <Seq> [params]");
            System.exit(1);
        }
        if (args[0].equals("-a")) {
            doAll = true;
            argNum++;
        }
        urlStr = args[argNum++];
        extId = args[argNum++];
        if (((args.length - argNum) % 2) != 0) {
            System.out.println("Usage: TestApolloClient [-a] <IOR url> <Seq> [params]");
            System.exit(1);
        }
        System.out.println("URL:    " + urlStr);
        System.out.println("Region: " + extId);
        if (doAll) {
            System.out.println("!!! Doing all tests !!!");
        }
        sessParams = new Param[(args.length - argNum) / 2];
        for (int i = argNum, j = 0; i < args.length; i += 2, j++) {
            sessParams[j] = new Param(args[i], args[i + 1]);
            System.out.println("Added parameter: " + sessParams[j].name + " " + sessParams[j].value);
        }
        try {
            System.out.println("Creating orb\n");
            orb = org.omg.CORBA.ORB.init(args, new java.util.Properties());
        } catch (org.omg.CORBA.SystemException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        try {
            java.net.URL url1 = new java.net.URL(urlStr);
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(url1.openStream()));
            String refIOR = in.readLine();
            org.omg.CORBA.Object manObj = orb.string_to_object(refIOR);
            mySessMan = SessionManagerHelper.narrow(manObj);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        Param[] nullParams = new Param[0];
        Session mySess = mySessMan.initiate_Session(nullParams);
        try {
            mySess.connect(sessParams);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        AnnotatedRegion annSeq = null;
        AnnotatedGene[] genes;
        Analysis[] analyses;
        GenericAnnotation[] genannot;
        System.out.println("Getting region now");
        try {
            annSeq = mySess.get_AnnotatedRegion(extId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
        System.out.println("Getting data now");
        try {
            if (doAll) {
                System.out.println(" Annotation Types");
                String[] types = annSeq.get_annotation_type_list();
                for (int i = 0; i < types.length; i++) {
                    System.out.println("  Id " + i + ": " + types[i]);
                }
            } else {
                System.out.println(" SKIPPING Annotation Types");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        try {
            System.out.println(" Sequence");
            String seq = annSeq.sequence_as_string();
            System.out.println("  Sequence = " + seq);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        try {
            System.out.println(" Genes");
            genes = annSeq.get_gene_list();
            System.out.println("  Number of genes = " + genes.length);
            for (int i = 0; i < genes.length; i++) {
                System.out.println("  Id " + i + ": " + genes[i].ident.name);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        try {
            System.out.println(" Analyses");
            analyses = annSeq.get_analysis_list();
            System.out.println("  Number of analyses = " + analyses.length);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        try {
            if (doAll) {
                System.out.println(" Generic Annotations");
                genannot = annSeq.get_generic_annotation_list();
                System.out.println("  Number of generic annotations = " + genannot.length);
            } else {
                System.out.println(" SKIPPING Generic Annotations");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
