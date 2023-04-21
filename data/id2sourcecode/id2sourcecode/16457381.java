    private void serveClient(final ObjectInputStream in, final ObjectOutputStream out) throws Exception {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            switch(in.readInt()) {
                case CMD_GC:
                    System.gc();
                    out.writeInt(COMMAND_ACK);
                    out.flush();
                    break;
                case CMD_SNAPSHOT:
                    out.writeInt(COMMAND_ACK);
                    ThreadProfiler.createSnapshot(out);
                    out.flush();
                    break;
                case CMD_RESET_STATS:
                    ThreadProfiler.resetStats();
                    out.writeInt(COMMAND_ACK);
                    out.flush();
                    break;
                case CMD_APPLY_RULES:
                    out.writeInt(COMMAND_ACK);
                    out.flush();
                    String opts = in.readUTF();
                    String rules = in.readUTF();
                    final int[] progress = new int[1];
                    Transformer.Callback callback = new Transformer.Callback() {

                        public void notifyClassTransformed(String className, int backSequence, int bachSize) {
                            progress[0] = backSequence;
                        }
                    };
                    int n = Agent.startNewSession(opts, rules, callback);
                    out.writeInt(n);
                    out.flush();
                    while (progress[0] < n) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                        out.writeInt(progress[0]);
                        out.flush();
                    }
                    out.writeInt(-1);
                    out.flush();
                    synchronized (Agent.waitConnectionLock) {
                        Agent.waitConnectionLock.notifyAll();
                    }
                    break;
                case CMD_LIST_CLASSES:
                    out.writeInt(COMMAND_ACK);
                    Class[] classes = Agent.getLoadedClasses(true);
                    out.writeInt(classes.length);
                    synchronized (Agent.modifiedClassNames) {
                        for (int i = 0; i < classes.length; i++) {
                            out.writeUTF(classes[i].getName());
                            final boolean instrumented = Agent.modifiedClassNames.contains(classes[i].getName());
                            out.writeBoolean(instrumented);
                        }
                    }
                    out.flush();
                    break;
                case CMD_GET_RUNTIME_INFO:
                    out.writeInt(COMMAND_ACK);
                    out.writeUTF(Agent.rtbean.getBootClassPath());
                    out.writeUTF(Agent.rtbean.getClassPath());
                    writeStringList(out, Agent.rtbean.getInputArguments());
                    out.writeUTF(Agent.rtbean.getLibraryPath());
                    out.writeUTF(Agent.rtbean.getName());
                    out.writeUTF(Agent.rtbean.getVmName());
                    out.writeLong(Agent.rtbean.getStartTime());
                    out.writeLong(Agent.rtbean.getUptime());
                    writeStringMap(out, Agent.rtbean.getSystemProperties());
                    out.flush();
                    break;
                case CMD_GET_MEMORY_INFO:
                    out.writeInt(COMMAND_ACK);
                    writeMemoryUsage(out, Agent.membean.getHeapMemoryUsage());
                    writeMemoryUsage(out, Agent.membean.getNonHeapMemoryUsage());
                    out.writeInt(Agent.membean.getObjectPendingFinalizationCount());
                    out.flush();
                    break;
                case CMD_GET_THREAD_INFO:
                    out.writeInt(COMMAND_ACK);
                    out.flush();
                    long[] ids = (long[]) in.readUnshared();
                    int maxDepth = in.readInt();
                    ids = (ids == null) ? Agent.threadbean.getAllThreadIds() : ids;
                    out.writeUnshared(ServerUtil.makeSerializable(Agent.threadbean.getThreadInfo(ids, maxDepth)));
                    out.flush();
                    break;
                case CMD_SET_THREAD_MONITORING:
                    boolean[] flags = (boolean[]) in.readUnshared();
                    boolean[] support = new boolean[] { Agent.threadbean.isThreadContentionMonitoringSupported(), Agent.threadbean.isThreadCpuTimeSupported() };
                    if (support[0]) {
                        Agent.threadbean.setThreadContentionMonitoringEnabled(flags[0]);
                    }
                    if (support[1]) {
                        Agent.threadbean.setThreadCpuTimeEnabled(flags[1]);
                    }
                    out.writeUnshared(support);
                    out.flush();
                    break;
                case CMD_DISCONNECT:
                    out.writeInt(COMMAND_ACK);
                    out.flush();
                    return;
                default:
                    out.writeInt(STATUS_UNKNOWN_CMD);
            }
        }
    }
