    public static void main(String[] args) {
        String xmlDir = System.getProperty("user.dir");
        boolean parallel = true;
        boolean append = false;
        boolean testing = false;
        String logFileName = "SPE_Log.log";
        for (int i = 0; i < args.length; i = i + 2) {
            if (args[i].equalsIgnoreCase("-d")) {
                String folderName = args[i + 1];
                File f = new File(folderName);
                if (f.exists() && f.isDirectory()) {
                    xmlDir = f.getAbsolutePath();
                }
                f = null;
            }
            if (args[i].equalsIgnoreCase("-p")) {
                if (args[i + 1].equalsIgnoreCase("on")) {
                    parallel = true;
                } else {
                    parallel = false;
                }
            }
            if (args[i].equalsIgnoreCase("-a")) {
                if (args[i + 1].equalsIgnoreCase("yes")) {
                    append = true;
                }
            }
            if (args[i].equalsIgnoreCase("-l")) {
                logFileName = args[i + 1];
            }
            if (args[i].equalsIgnoreCase("-t")) {
                if (args[i + 1].equalsIgnoreCase("yes")) testing = true; else testing = false;
            }
        }
        FileHandler hand = null;
        try {
            hand = new FileHandler(logFileName, append);
        } catch (Exception e) {
            System.out.println("The specified log file cannot be created, please check the path specified: " + logFileName);
            System.out.println("A generic log file with name SPE_Log.log is generated in the working directory.");
            try {
                hand = new FileHandler("SPE_Log.log", append);
            } catch (Exception e1) {
                System.out.println("The file cannot be created, please read the following information for further details.");
                e1.printStackTrace();
                System.exit(0);
            }
        }
        hand.setFormatter(new SimpleFormatter());
        log = Logger.getLogger("SqlParallelExecuter_Logger");
        log.setUseParentHandlers(false);
        log.addHandler(hand);
        log.setLevel(Level.ALL);
        log.info("SqlParallelExecuter started.");
        System.out.println("SQL Parallel Executer Copyright (C) 2011  Emiliano Marin");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
        System.out.println("This is free software, and you are welcome to redistribute it");
        System.out.println("under certain conditions.");
        System.out.println("Welcome! The SqlParallelExecuter is going to process your SQL scripts...");
        System.out.println("The scripts are located in this folder: " + xmlDir);
        if (parallel == true) {
            System.out.println("SqlParallelExecuter runs scripts in parallel");
            log.info("SqlParallelExecuter is run in parallel");
        } else {
            System.out.println("SqlParallelExecuter runs scripts sequentially");
            log.info("SqlParallelExecuter is run sequentially");
        }
        if (testing == true) {
            System.out.println("SqlParallelExecuter runs in testing mode");
            log.info("SqlParallelExecuter is run in testing mode");
        } else {
            System.out.println("SqlParallelExecuter runs in normal mode");
            log.info("SqlParallelExecuter is run in normal mode");
        }
        log.info("xml files are searched in " + xmlDir);
        if (!CheckFileExists(xmlDir + "\\scripts.xml")) {
            System.out.println("The file scripts.xml is not available in: " + xmlDir);
            System.out.println("SQL Parallel Executer stops.");
            System.exit(0);
        }
        if (!CheckFileExists(xmlDir + "\\SQLscripts.xml")) {
            System.out.println("The file SQLscripts.xml is not available in: " + xmlDir);
            System.out.println("SQL Parallel Executer stops.");
            System.exit(0);
        }
        String[] SQLscripts = loadSqlScripts(xmlDir);
        Document doc = getDocument(xmlDir + "\\scripts.xml");
        Element root = doc.getDocumentElement();
        Element sqlScript = (Element) root.getFirstChild();
        ArrayList<Script> scripts = new ArrayList<Script>();
        while (sqlScript != null) {
            SQLScript s = getScript(sqlScript);
            sqlScript = (Element) sqlScript.getNextSibling();
            scripts.add(new Script(SQLscripts[s.getSqlScript()], s.getDSN(), s.getName(), s.getDayOfWeek(), s.getStaticParameter(), s.getDynamicParameter(), s.getDynamicParameterReference(), testing, s.getDepends()));
        }
        ArrayList<Thread> tScripts = new ArrayList<Thread>();
        for (Runnable e : scripts) tScripts.add(new Thread(e));
        new Thread(new SqlExecuterConsole(scripts)).start();
        for (Thread e : tScripts) {
            e.start();
            if (!parallel) {
                try {
                    e.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        for (Thread e : tScripts) {
            try {
                e.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("All jobs have been completed.");
        log.info("SqlParallelExecutor completed its jobs.");
    }
