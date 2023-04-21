    protected void readUserCommands() throws IOException {
        AsyncExecute.sleepTight(500);
        System.out.println("Entering interactive mode... type Selenium commands here (e.g: cmd=open&1=http://www.yahoo.com)");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        final String[] lastSessionId = new String[] { "" };
        while ((userInput = stdIn.readLine()) != null) {
            userInput = userInput.trim();
            if ("exit".equals(userInput) || "quit".equals(userInput)) {
                System.out.println("Stopping...");
                stop();
                System.exit(0);
            }
            if ("".equals(userInput)) continue;
            if (!userInput.startsWith("cmd=") && !userInput.startsWith("commandResult=")) {
                System.err.println("ERROR -  Invalid command: \"" + userInput + "\"");
                continue;
            }
            final boolean newBrowserSession = userInput.indexOf("getNewBrowserSession") != -1;
            if (userInput.indexOf("sessionId") == -1 && !newBrowserSession) {
                userInput = userInput + "&sessionId=" + lastSessionId[0];
            }
            final URL url = new URL("http://localhost:" + configuration.getPort() + "/selenium-server/driver?" + userInput);
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        LOGGER.info("---> Requesting " + url.toString());
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buffer = new byte[2048];
                        int length;
                        while ((length = is.read(buffer)) != -1) {
                            out.write(buffer, 0, length);
                        }
                        is.close();
                        String output = out.toString();
                        if (newBrowserSession) {
                            if (output.startsWith("OK,")) {
                                lastSessionId[0] = output.substring(3);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        if (debugMode) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
        }
    }
