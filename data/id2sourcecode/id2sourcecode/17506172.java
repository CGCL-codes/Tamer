    public void PCPERFORM(MaverickString result, ConstantString command) throws MaverickException {
        String[] params = { command.toString() };
        String shellArgs = properties.getProperty(PROP_SHELL_ARGS, DEFAULT_SHELL_ARGS);
        String args = MessageFormat.format(shellArgs, (Object[]) params);
        if (result != null) {
            result.clear();
            getChannel(SCREEN_CHANNEL).pushWriter(new StringWriter(result));
        }
        StringTokenizer st = new StringTokenizer(args);
        String[] comm = new String[st.countTokens() + 1];
        int index = 0;
        comm[index++] = properties.getProperty(PROP_SHELL, DEFAULT_SHELL);
        while (st.hasMoreElements()) {
            comm[index++] = MessageFormat.format(st.nextToken(), (Object[]) params);
        }
        try {
            Process p = Runtime.getRuntime().exec(comm);
            Thread out = new ConnectStream(new InputStreamReader(p.getInputStream()), getChannel(SCREEN_CHANNEL).peekWriter());
            Thread err = new ConnectStream(new InputStreamReader(p.getErrorStream()), getChannel(SCREEN_CHANNEL).peekWriter());
            err.start();
            out.start();
            boolean finished = false;
            while (!finished) {
                try {
                    int exitValue = p.waitFor();
                    finished = true;
                } catch (InterruptedException ie) {
                }
            }
            try {
                out.join();
                err.join();
            } catch (InterruptedException ie) {
                throw new MaverickException(0, ie);
            }
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
        if (result != null) {
            getChannel(SCREEN_CHANNEL).popWriter();
            result.CHANGE(result, ConstantString.LINE_SEPARATOR, ConstantString.AM, ConstantString.ZERO, ConstantString.ZERO);
        }
    }
