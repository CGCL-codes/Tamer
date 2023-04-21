    public TestJNDIOps(String fileName, String url, String user, String pwd, boolean tracing, String version, boolean debugFlag, boolean terminateFlag, String referral, boolean useSSL, boolean printstackFlag) {
        out = System.out;
        debug = debugFlag;
        terminating = terminateFlag;
        printstack = printstackFlag;
        if (url != null) openConnection(url, user, pwd, tracing, version, referral, useSSL);
        if (fileName != null) in = openFile(fileName); else in = new BufferedReader(new InputStreamReader(System.in));
    }
