    public int execute(String args[], MdShellCommandThread commandThread) {
        MdShellEnv env = commandThread.getEnv();
        PrintWriter writer = new PrintWriter(commandThread.getOut());
        if (args == null) {
            Iterator it;
            writer.println("Available commands are following./n");
            writer.println("Internal commands:");
            writer.print("\t");
            it = env.getInternalCommandIterator();
            while (it.hasNext()) {
                writer.print((String) it.next() + " ");
            }
            writer.println("\n");
            writer.println("File access commands:");
            writer.print("\t");
            it = env.getFileAccessCommandIterator();
            while (it.hasNext()) {
                writer.print((String) it.next() + " ");
            }
            writer.println("\n");
            writer.println("Network commands:");
            writer.print("\t");
            it = env.getNetworkCommandIterator();
            while (it.hasNext()) {
                writer.print((String) it.next() + " ");
            }
            writer.println("\n");
            writer.println("Extra commands:");
            writer.print("\t");
            it = env.getExtraCommandIterator();
            while (it.hasNext()) {
                writer.print((String) it.next() + " ");
            }
            writer.println("\n");
        } else {
            MdShellCommand com = env.getCommand(args[0]);
            writer.println(com.getDescription());
        }
        writer.flush();
        return 0;
    }
