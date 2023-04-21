    private void invoke(String[] params) {
        if (poller.isShuttingDown()) return;
        String[] params2 = new String[params.length + shellCmd.length + 1];
        System.arraycopy(shellCmd, 0, params2, 0, shellCmd.length);
        params2[shellCmd.length] = scriptPath;
        System.arraycopy(params, 0, params2, shellCmd.length + 1, params.length);
        try {
            if (poller.isVerbose()) {
                System.err.println("Attempting to run " + composeCmdString(params));
            }
            Process p = Runtime.getRuntime().exec(params2);
            InputStream os = p.getInputStream();
            InputStream os2 = p.getErrorStream();
            int c;
            try {
                while ((c = os.read()) != -1) System.out.write(c);
            } catch (IOException e) {
                System.err.println("Problem reading script output for " + composeCmdString(params));
            }
            try {
                while ((c = os2.read()) != -1) System.err.write(c);
            } catch (IOException e) {
                System.err.println("Problem reading script error output for " + composeCmdString(params));
            }
            try {
                int result = p.waitFor();
                if (result != 0) System.err.println(composeCmdString(params) + " returned nonzero");
            } catch (InterruptedException e) {
                System.err.println("ScriptedPollManager interrupted while waiting for child process " + composeCmdString(params));
            }
        } catch (IOException e) {
            System.err.println("Problem invoking " + composeCmdString(params));
        }
    }
