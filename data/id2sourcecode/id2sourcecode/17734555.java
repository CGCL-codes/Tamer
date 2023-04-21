    public DoubleMatrix2D getPrecedentInfo(int distance, LogReader log) {
        int numSimilarPIs = 0;
        log.reset();
        modelElements = log.getLogSummary().getLogEvents();
        System.out.println("walid");
        boolean update = false;
        int s = modelElements.size();
        DoubleMatrix2D D = DoubleFactory2D.sparse.make(s, s, 0);
        while (log.hasNext()) {
            ProcessInstance pi = log.next();
            numSimilarPIs = MethodsForWorkflowLogDataStructures.getNumberSimilarProcessInstances(pi);
            int[] memory = new int[distance];
            int i = 0;
            AuditTrailEntries ates = pi.getAuditTrailEntries();
            while (ates.hasNext()) {
                AuditTrailEntry ate = ates.next();
                if (i < distance) {
                    memory[i] = modelElements.findLogEventNumber(ate.getElement(), ate.getType());
                    i++;
                    continue;
                }
                int index = modelElements.findLogEventNumber(ate.getElement(), ate.getType());
                if (distance == 0) {
                    D.set(index, index, D.get(index, index) + numSimilarPIs);
                } else {
                    D.set(index, memory[0], D.get(index, memory[0]) + numSimilarPIs);
                    for (int j = 0; j < distance - 1; j++) {
                        memory[j] = memory[j + 1];
                    }
                    memory[distance - 1] = index;
                }
            }
        }
        return D;
    }
