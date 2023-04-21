    public WaitGraph(Map<Long, ThreadInfo> threads, PrintWriter writer) {
        _cycles = new ArrayList<List<ThreadInfo>>();
        _map = new HashMap<Long, MapEntry>();
        for (ThreadInfo ti : threads.values()) {
            _map.put(ti.getThreadID(), new MapEntry(ti, MapEntry.Color.WHITE, 0, null));
        }
        for (ThreadInfo ti : threads.values()) {
            MapEntry me = _map.get(ti.getThreadID());
            if (me.getColor() == MapEntry.Color.WHITE) {
                bfs(ti, threads);
            }
        }
        if (_cycles.size() > 0) {
            writer.println("Deadlocks found:");
            int num = 1;
            for (List<ThreadInfo> cycle : _cycles) {
                writer.println("\tCycle " + (num++));
                for (ThreadInfo ti : cycle) {
                    writer.println("\t\tThread " + ti.toStringVerbose());
                }
            }
        }
    }
