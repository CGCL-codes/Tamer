    public static void main(String[] args) {
        Properties props = parseArgs(args, RemoteSudo.getDefaults());
        if (props == null) {
            System.err.println("Unable to parse properties!");
            RemoteSudo.usage();
            System.exit(1);
        }
        if ("true".equals(props.getProperty("rsudo.help"))) {
            RemoteSudo.usage();
            System.exit(0);
        }
        if (props.getProperty("rsudo.hosts") == null) {
            System.err.println("you must specify at least one host!");
            RemoteSudo.usage();
            System.exit(2);
        }
        if ((props.getProperty("rsudo.remotecmd") == null) && ("false".equals(props.getProperty("rsudo.put")))) {
            System.err.println("you must specify a command/script to run or a file to put");
            RemoteSudo.usage();
            System.exit(3);
        }
        if ("true".equals(props.getProperty("rsudo.dumpprops"))) {
            try {
                props.store(System.out, "# RemoteSudo properties file");
                System.exit(0);
            } catch (IOException ioe) {
                System.err.println("unable to dump properties file");
                System.exit(1);
            }
        }
        String password = null;
        String tmp = props.getProperty("stdin");
        if (tmp != null && "true".equals(tmp)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            try {
                password = in.readLine();
                in.close();
            } catch (IOException ioe) {
                System.err.println("error reading password");
                System.exit(1);
            }
        } else {
            password = MaskingPasswordReader.readPassword();
        }
        props.setProperty("rsudo.password", password);
        Hashtable<String, Entry> creds = null;
        tmp = props.getProperty("keepass.kdb");
        if (tmp != null) {
            Database db = new Database(tmp);
            MessageDigest sha256 = null;
            try {
                sha256 = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            }
            db.setMasterKey(sha256.digest(password.getBytes()));
            db.setPasswordHash(sha256.digest(password.getBytes()));
            try {
                db.decrypt();
            } catch (InvalidCipherTextException e1) {
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            creds = new Hashtable<String, Entry>(db.getEntries().length);
            for (Entry e : db.getEntries()) {
                creds.put(e.getUrl(), e);
            }
        }
        StringTokenizer st = new StringTokenizer(props.getProperty("rsudo.hosts"), ",:");
        int hostCount = st.countTokens();
        if (hostCount == 0) {
            System.err.println("you must specify at least one host!");
            RemoteSudo.usage();
            System.exit(1);
        }
        int delay = -1;
        try {
            delay = Integer.parseInt(props.getProperty("rsudo.delay"));
        } catch (Exception e) {
            System.err.println("rsudo: warning: unable to parse delay; continuing without one");
            delay = -1;
        }
        int timeout = 0;
        try {
            timeout = Integer.parseInt(props.getProperty("rsudo.timeout"));
        } catch (Exception e) {
            System.err.println("rsudo: warning: unable to parse timeout; continuing without one");
            timeout = 0;
        }
        timeout = ((timeout >= 0) ? (timeout * 1000) : 0);
        boolean sync = "true".equals(props.getProperty("rsudo.synchronous")) ? true : false;
        long checkTimer = 0;
        if (props.getProperty("rsudo.check") != null) {
            try {
                checkTimer = Long.parseLong(props.getProperty("rsudo.check"));
            } catch (Exception e) {
                System.err.println("rsudo: warning: unable to parse check timer; continuing without one");
                checkTimer = 0;
            }
        }
        int threadCount = hostCount;
        if (props.getProperty("rsudo.threads") != null) {
            if (sync) {
                System.err.println("rsudo: warning: --sync and --threads both specified; ignoring --threads argument");
            } else {
                try {
                    threadCount = Integer.parseInt(props.getProperty("rsudo.threads"));
                } catch (Exception e) {
                    System.err.println("rsudo: warning: unable to parse thread count; continuing using thread count of " + threadCount);
                }
            }
        }
        if (sync) threadCount = 1;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        RemoteSudo[] rsudos = new RemoteSudo[hostCount];
        for (int i = 0; i < hostCount; i++) {
            String host = st.nextToken();
            rsudos[i] = (creds != null) ? new RemoteSudo(host, props, creds) : new RemoteSudo(host, props);
            service.submit(rsudos[i]);
            if (checkTimer > 0) {
                new Thread(new RemoteSudoMonitor(rsudos[i], (checkTimer * 1000))).start();
            }
            if (delay > 0) {
                try {
                    Thread.sleep(delay * 1000);
                } catch (InterruptedException e) {
                    System.err.println("rsudo: warning: unable to delay; continuing without delay to next host");
                }
            }
        }
        service.shutdown();
        if (timeout > 0) {
            try {
                service.awaitTermination(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        } else {
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
            }
        }
        System.exit(0);
    }
