    public int process(MdShellCommandThread commandThread, String[] strArgs) {
        MdShellEnv shellenv = commandThread.getEnv();
        PrintWriter writer = new PrintWriter(commandThread.getOut());
        Vector vecList = buildData(shellenv, strArgs);
        if (vecList == null) {
            return -2;
        }
        showData(shellenv, vecList, writer);
        return 0;
    }
