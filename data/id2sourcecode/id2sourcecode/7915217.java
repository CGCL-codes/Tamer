    static UnixProcess createUnixProcess(String[] commands, Map<String, String> envs, String workDirectory, ProcessIo pio) throws IOException {
        String prog = commands[0];
        byte[][] args = new byte[commands.length - 1][];
        int size = 0;
        for (int i = 0; i < args.length; i++) {
            args[i] = commands[i + 1].getBytes();
            size += (args[i].length + 1);
        }
        int argCount = args.length;
        byte[] argBlock = new byte[size];
        int tmpOfs = 0;
        for (int k = 0; k < args.length; k++) {
            byte[] arg = args[k];
            System.arraycopy(arg, 0, argBlock, tmpOfs, arg.length);
            tmpOfs += (arg.length + 1);
        }
        byte[][] envBytes = new byte[envs.size()][];
        int envBlockSize = 0;
        int envOfs = 0;
        for (Map.Entry<String, String> entry : envs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String envEntry = key + "=" + value;
            byte[] envEntryBytes = envEntry.getBytes();
            envBytes[envOfs++] = envEntryBytes;
            envBlockSize += (envEntryBytes.length + 1);
        }
        byte[] envBlock = new byte[envBlockSize];
        envOfs = 0;
        for (int i = 0; i < envBytes.length; i++) {
            System.arraycopy(envBytes[i], 0, envBlock, envOfs, envBytes[i].length);
            envOfs += (envBytes[i].length + 1);
        }
        int pid = createProcess(NativeUtil.toCString(prog), argBlock, argCount, envBlock, envBytes.length, NativeUtil.toCString(workDirectory), pio);
        UnixProcess process = new UnixProcess(pid);
        return process;
    }
