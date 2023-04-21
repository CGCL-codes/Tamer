    public void compile(String args[]) throws Exception {
        long parseTime, compileTime, startTime = System.currentTimeMillis();
        int words;
        String configFile = null;
        Vector tmpfiles = new Vector();
        Vector files = new Vector();
        String file;
        int size;
        sourcepath = "";
        boolean nostopwords = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-db")) {
                if ((i + 1) < args.length) {
                    dbName = args[++i];
                    if (dbName.lastIndexOf("/") != dbName.length() - 1) {
                        dbName = dbName.concat("/");
                    }
                } else {
                    System.out.println(args[i] + "-db requires argument");
                }
            } else if (args[i].equals("-sourcepath")) {
                if ((i + 1) < args.length) {
                    sourcepath = args[++i];
                } else {
                    System.out.println(args[i] + "-sourcepath requires argument");
                }
            } else if (args[i].equals("-locale")) {
                if ((i + 1) < args.length) {
                    defaultLang = args[++i];
                } else {
                    System.out.println(args[i] + "-locale requires argument");
                }
            } else if (args[i].equals("-logfile")) {
                if ((i + 1) < args.length) {
                    String logFile = args[++i];
                    try {
                        logStream = new PrintStream(new FileOutputStream(logFile), true);
                        System.setErr(logStream);
                        System.setOut(logStream);
                        verbose = logStream;
                    } catch (java.io.FileNotFoundException ex) {
                        System.out.println("Couldn't create logFile " + logFile);
                    }
                } else {
                    System.out.println(args[i] + "-logfile requires argument");
                }
            } else if (args[i].equals("-verbose")) verbose = System.out; else if (args[i].equals("-nostopwords")) nostopwords = true; else if (args[i].equals("-c")) {
                if ((i + 1) < args.length) {
                    configFile = args[++i];
                } else {
                    System.out.println(args[i] + "-c requires argument");
                }
            } else if (args[i].startsWith("-")) {
                System.out.println("Unknown argument '" + args[i] + "'");
                showUsage();
                return;
            } else {
                tmpfiles.addElement(args[i]);
            }
        }
        config = new ConfigFile(configFile, files, nostopwords);
        files = config.getFiles();
        size = tmpfiles.size();
        for (int i = 0; i < size; i++) {
            files = loadFiles((String) tmpfiles.elementAt(i), files);
        }
        size = files.size();
        if (size == 0) {
            System.out.println("No files specified to index");
            showUsage();
            return;
        }
        indexBuilder = new DefaultIndexBuilder(dbName);
        indexBuilder.storeStopWords(config.getStopWords());
        for (int i = 0; i < size; i++) {
            file = (String) files.elementAt(i);
            URL url = new URL("file", "", sourcepath + file);
            InputStream in = url.openStream();
            URLConnection conn = url.openConnection();
            String type = conn.getContentType();
            setContentType(type);
            if (kit != null) {
                try {
                    if (verbose != null) {
                        verbose.println("   File: '" + file + "'");
                        verbose.println("    URL: '" + config.getURLString(file) + "'");
                    }
                    parseFile(in, file, false);
                    in.close();
                } catch (UnsupportedEncodingException e1) {
                    System.out.println("File: '" + file + "' encoding " + charSetName + " not supported");
                    in.close();
                    continue;
                } catch (IOException e) {
                    if (debugFlag) e.printStackTrace();
                    System.out.println("I/O exception occurred in file '" + sourcepath + file + "'");
                    in.close();
                    continue;
                }
            }
        }
        parseTime = System.currentTimeMillis() - startTime;
        compileTime = System.currentTimeMillis() - startTime - parseTime;
        if (verbose != null) {
            verbose.println("        Parse time: " + (float) parseTime / 1000.0 + " s");
        }
        indexBuilder.close();
    }
