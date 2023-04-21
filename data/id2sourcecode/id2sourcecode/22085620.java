    public void execute(String s, PrintStream out, PrintStream err) {
        StringTokenizer st = new StringTokenizer(s, " ");
        if (st.countTokens() == 1) {
            printUsage(out);
            return;
        }
        st.nextToken();
        String token = null;
        boolean stopOnError = false;
        boolean echoCommand = false;
        boolean prompt = false;
        boolean novar = false;
        String urlstr = null;
        while (st.hasMoreTokens()) {
            token = st.nextToken().trim();
            if (token.equals(STOP_OPTION)) {
                stopOnError = true;
            } else if (token.equals(ECHO_OPTION)) {
                echoCommand = true;
            } else if (token.equals(PROMPT_OPTION)) {
                prompt = true;
            } else if (token.equals(NOVAR_OPTION)) {
                novar = true;
            } else if (token.equals(HELP_OPTION)) {
                break;
            } else {
                urlstr = token;
            }
        }
        if (urlstr == null) {
            printUsage(out);
            return;
        }
        urlstr = SubstituteUtility.substitute(urlstr, shellContext);
        URL url;
        try {
            url = new URL(urlstr);
        } catch (MalformedURLException e) {
            e.printStackTrace(err);
            err.flush();
            return;
        }
        URLConnection urlcnx;
        BufferedReader br;
        try {
            urlcnx = url.openConnection();
            br = new BufferedReader(new InputStreamReader(urlcnx.getInputStream()));
        } catch (IOException ioe) {
            err.println("cannot read script file '" + urlstr + "': " + ioe.getMessage());
            err.flush();
            return;
        }
        ByteArrayOutputStream baerr = new ByteArrayOutputStream();
        PrintStream pbaerr = new PrintStream(baerr);
        try {
            String commandLine;
            int lineNumber = 0;
            while ((commandLine = br.readLine()) != null) {
                ++lineNumber;
                commandLine = commandLine.trim();
                if (commandLine.equals("")) continue;
                if (!novar) {
                    commandLine = SubstituteUtility.substitute(commandLine, shellContext);
                }
                if (prompt) out.print("-->");
                if (echoCommand) out.print(commandLine);
                if (prompt || echoCommand) out.println();
                shellService.executeCommand(commandLine, out, pbaerr);
                pbaerr.flush();
                byte[] ba = baerr.toByteArray();
                if (ba.length != 0) {
                    err.println("error in line " + lineNumber + ": " + new String(ba));
                    err.flush();
                    if (stopOnError) break;
                    baerr.reset();
                }
            }
        } catch (IOException ioe) {
            err.println(ioe.getMessage());
            err.flush();
            return;
        } catch (Exception e) {
            err.println(e.getMessage());
            err.flush();
            return;
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
            }
        }
    }
