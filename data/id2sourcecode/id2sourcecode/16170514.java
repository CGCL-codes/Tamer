    protected void redirectSysOut() throws IOException {
        log.debug("redirectSysOut");
        sysOut = System.out;
        final PipedInputStream in = new PipedInputStream();
        consoleOut = new PipedOutputStream(in);
        System.setOut(new PrintStream(consoleOut));
        final IOConsoleOutputStream console = VaultClipsePlugin.getDefault().getConsole().newOutputStream();
        console.setEncoding("UTF-8");
        writerThread = new Thread() {

            public void run() {
                log.debug("Starting input listener");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    PrintStream writer = new PrintStream(console);
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        if (!isError && line.contains("[ERROR]")) {
                            isError = true;
                            message = line;
                        }
                        writer.println(line);
                    }
                } catch (IOException e) {
                    log.error("Exception reading output stream", e);
                }
                log.debug("Input reader closing");
            }
        };
        writerThread.start();
    }
