    public CompactWaitGraph(Map<Long, CompactThreadInfo> threads, PrintWriter writer) {
        _writer = writer;
        _cycles = new ArrayList<List<CompactThreadInfo>>();
        _map = new HashMap<Long, CompactWaitGraph.MapEntry>();
        for (CompactThreadInfo cti : threads.values()) {
            _map.put(cti.getThreadID(), new MapEntry(cti, MapEntry.Color.WHITE, 0, null));
        }
        for (CompactThreadInfo cti : threads.values()) {
            MapEntry me = _map.get(cti.getThreadID());
            if (me.getColor() == MapEntry.Color.WHITE) {
                bfs(cti, threads);
            }
        }
        if (_cycles.size() > 0) {
            _writer.println("// Deadlocks found:");
            int num = 1;
            for (List<CompactThreadInfo> cycle : _cycles) {
                _writer.println("// \tCycle " + (num++));
                for (CompactThreadInfo ti : cycle) {
                    _writer.println("// \t\tThread " + ti.toString());
                }
            }
        }
    }
