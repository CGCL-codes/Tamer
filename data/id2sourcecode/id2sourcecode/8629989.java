    public XQTSExecuter(String args[]) throws Exception {
        System.out.println("Options: " + java.util.Arrays.asList(args));
        if (args.length == 0) {
            printHelp();
        }
        boolean overwrite = false;
        includes = new ArrayList<String>();
        excludes = new HashSet<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-output:")) {
                String s = args[i];
                s = s.substring(s.indexOf(":") + 1);
                File outFile = new File(s);
                if (outFile.exists() && !overwrite) throw new IllegalArgumentException("The output file already exists!");
                outFile.getParentFile().mkdirs();
                out = new PrintWriter(new FileWriter(outFile));
            } else if (args[i].equalsIgnoreCase("-overwrite")) {
                overwrite = true;
            } else if (args[i].startsWith("-include:")) {
                String s = args[i];
                s = s.substring(s.indexOf(":") + 1);
                StringTokenizer tok = new StringTokenizer(s, ",");
                while (tok.hasMoreTokens()) {
                    String token = tok.nextToken().toLowerCase();
                    System.err.println("including: " + token);
                    includes.add(token);
                }
            } else if (args[i].startsWith("-exclude:")) {
                String s = args[i];
                s = s.substring(s.indexOf(":") + 1);
                StringTokenizer tok = new StringTokenizer(s, ",");
                while (tok.hasMoreTokens()) {
                    excludes.add(tok.nextToken().toLowerCase());
                }
            } else if (args[i].equals("-?") || args[i].equals("-h") || args[i].equals("-help")) {
                printHelp();
            } else if (i == args.length - 1) {
                xqtsRoot = new File(args[i]);
                File catalogFile = new File(xqtsRoot, "XQTSCatalog.xml");
                if (!catalogFile.exists() || !catalogFile.canRead()) throw new IllegalArgumentException("Cannot read catalog file: " + args[i]);
                catalog = new PDOMFactory(new FileDataServerFactory()).create();
                SAXParserFactory fac = SAXParserFactory.newInstance();
                fac.setNamespaceAware(true);
                PDOMParserFactory parserFac = new PDOMParserFactory();
                parserFac.setSAXParserFactory(fac);
                PDOMParser parser = parserFac.newParser();
                System.err.println("Parsing catalog...");
                parser.parse(catalogFile, catalog);
                System.err.println("Done.");
            } else {
                System.err.println("Unrecognized option: " + args[i]);
                printHelp();
            }
        }
        if (catalog == null) {
            throw new IllegalArgumentException("Missing xqts root argument!");
        } else if (includes.isEmpty()) {
            throw new IllegalArgumentException("No test-groups specified! Use the -include option to specify test-groups.");
        } else if (out == null) {
            File outFile = new File("XQTSResults.xml");
            if (outFile.exists() && !overwrite) throw new IllegalArgumentException("The default output file already exists!");
            out = new PrintWriter(new FileWriter(outFile));
            initOutFile();
        }
        System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl");
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        lsParser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
        lsInput = impl.createLSInput();
    }
