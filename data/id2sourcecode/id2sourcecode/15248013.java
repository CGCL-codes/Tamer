    public synchronized boolean[] setThreadMonitoring(boolean threadMonitoring, boolean cpuTimeMonitoring) throws ClientException {
        assertConnected();
        try {
            out.writeInt(AgentConstants.CMD_SET_THREAD_MONITORING);
            out.writeUnshared(new boolean[] { threadMonitoring, cpuTimeMonitoring });
            out.flush();
            return (boolean[]) in.readUnshared();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }
